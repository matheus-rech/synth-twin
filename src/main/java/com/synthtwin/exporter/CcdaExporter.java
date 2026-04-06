package com.synthtwin.exporter;

import com.synthtwin.model.*;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CcdaExporter implements Exporter {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public void export(List<Patient> patients, String outputDir) throws IOException {
        File ccdaDir = new File(outputDir, "ccda");
        ccdaDir.mkdirs();

        for (Patient patient : patients) {
            try {
                Document doc = buildCcda(patient);
                File outFile = new File(ccdaDir, patient.getId() + ".xml");
                writeXml(doc, outFile);
            } catch (Exception e) {
                throw new IOException("Failed to export CCDA for patient " + patient.getId(), e);
            }
        }
    }

    private Document buildCcda(Patient patient) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        Element root = doc.createElement("ClinicalDocument");
        root.setAttribute("xmlns", "urn:hl7-org:v3");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        doc.appendChild(root);

        // typeId
        Element typeId = doc.createElement("typeId");
        typeId.setAttribute("root", "2.16.840.1.113883.1.3");
        typeId.setAttribute("extension", "POCD_HD000040");
        root.appendChild(typeId);

        // templateId
        Element templateId = doc.createElement("templateId");
        templateId.setAttribute("root", "2.16.840.1.113883.10.20.22.1.1");
        root.appendChild(templateId);

        // id
        Element id = doc.createElement("id");
        id.setAttribute("root", patient.getId());
        root.appendChild(id);

        // code
        Element code = doc.createElement("code");
        code.setAttribute("code", "34117-2");
        code.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
        code.setAttribute("displayName", "History and Physical Note");
        root.appendChild(code);

        // title
        Element title = doc.createElement("title");
        title.setTextContent("Synthetic Patient Record");
        root.appendChild(title);

        // effectiveTime
        Element effectiveTime = doc.createElement("effectiveTime");
        effectiveTime.setAttribute("value", LocalDate.now().format(DATE_FMT));
        root.appendChild(effectiveTime);

        // confidentialityCode
        Element confCode = doc.createElement("confidentialityCode");
        confCode.setAttribute("code", "N");
        confCode.setAttribute("codeSystem", "2.16.840.1.113883.5.25");
        root.appendChild(confCode);

        // languageCode
        Element langCode = doc.createElement("languageCode");
        langCode.setAttribute("code", "en-US");
        root.appendChild(langCode);

        // recordTarget
        root.appendChild(buildRecordTarget(doc, patient));

        // component/structuredBody
        root.appendChild(buildStructuredBody(doc, patient));

        return doc;
    }

    private Element buildRecordTarget(Document doc, Patient patient) {
        Element recordTarget = doc.createElement("recordTarget");
        Element patientRole = doc.createElement("patientRole");

        Element id = doc.createElement("id");
        id.setAttribute("root", patient.getId());
        patientRole.appendChild(id);

        if (patient.getAddress() != null) {
            Element addr = doc.createElement("addr");
            Address a = patient.getAddress();
            Element streetAddr = doc.createElement("streetAddressLine");
            streetAddr.setTextContent(a.getStreet());
            addr.appendChild(streetAddr);
            Element city = doc.createElement("city");
            city.setTextContent(a.getCity());
            addr.appendChild(city);
            Element state = doc.createElement("state");
            state.setTextContent(a.getState());
            addr.appendChild(state);
            Element postalCode = doc.createElement("postalCode");
            postalCode.setTextContent(a.getZip());
            addr.appendChild(postalCode);
            Element country = doc.createElement("country");
            country.setTextContent("US");
            addr.appendChild(country);
            patientRole.appendChild(addr);
        }

        Element patientEl = doc.createElement("patient");

        Element name = doc.createElement("name");
        Element given = doc.createElement("given");
        given.setTextContent(patient.getFirstName());
        name.appendChild(given);
        Element family = doc.createElement("family");
        family.setTextContent(patient.getLastName());
        name.appendChild(family);
        patientEl.appendChild(name);

        Element genderCode = doc.createElement("administrativeGenderCode");
        genderCode.setAttribute("code", patient.getGender());
        genderCode.setAttribute("codeSystem", "2.16.840.1.113883.5.1");
        patientEl.appendChild(genderCode);

        Element birthTime = doc.createElement("birthTime");
        birthTime.setAttribute("value", patient.getBirthDate().format(DATE_FMT));
        patientEl.appendChild(birthTime);

        patientRole.appendChild(patientEl);
        recordTarget.appendChild(patientRole);
        return recordTarget;
    }

    private Element buildStructuredBody(Document doc, Patient patient) {
        Element component = doc.createElement("component");
        Element structuredBody = doc.createElement("structuredBody");

        structuredBody.appendChild(buildProblemsSection(doc, patient));
        structuredBody.appendChild(buildMedicationsSection(doc, patient));
        structuredBody.appendChild(buildAllergiesSection(doc, patient));
        structuredBody.appendChild(buildImmunizationsSection(doc, patient));
        structuredBody.appendChild(buildResultsSection(doc, patient));

        component.appendChild(structuredBody);
        return component;
    }

    private Element buildProblemsSection(Document doc, Patient patient) {
        Element component = doc.createElement("component");
        Element section = doc.createElement("section");

        Element templateId = doc.createElement("templateId");
        templateId.setAttribute("root", "2.16.840.1.113883.10.20.22.2.5.1");
        section.appendChild(templateId);

        Element code = doc.createElement("code");
        code.setAttribute("code", "11450-4");
        code.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
        code.setAttribute("displayName", "Problem List");
        section.appendChild(code);

        Element title = doc.createElement("title");
        title.setTextContent("Problems");
        section.appendChild(title);

        Element text = doc.createElement("text");
        StringBuilder sb = new StringBuilder();
        for (Condition c : patient.getConditions()) {
            sb.append(c.getDescription()).append(" (").append(c.getOnsetDate()).append(") ");
        }
        text.setTextContent(sb.toString());
        section.appendChild(text);

        component.appendChild(section);
        return component;
    }

    private Element buildMedicationsSection(Document doc, Patient patient) {
        Element component = doc.createElement("component");
        Element section = doc.createElement("section");

        Element templateId = doc.createElement("templateId");
        templateId.setAttribute("root", "2.16.840.1.113883.10.20.22.2.1.1");
        section.appendChild(templateId);

        Element code = doc.createElement("code");
        code.setAttribute("code", "10160-0");
        code.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
        code.setAttribute("displayName", "History of Medication Use");
        section.appendChild(code);

        Element title = doc.createElement("title");
        title.setTextContent("Medications");
        section.appendChild(title);

        Element text = doc.createElement("text");
        StringBuilder sb = new StringBuilder();
        for (Medication m : patient.getMedications()) {
            sb.append(m.getDescription()).append(" started ").append(m.getStartDate()).append("; ");
        }
        text.setTextContent(sb.toString());
        section.appendChild(text);

        component.appendChild(section);
        return component;
    }

    private Element buildAllergiesSection(Document doc, Patient patient) {
        Element component = doc.createElement("component");
        Element section = doc.createElement("section");

        Element templateId = doc.createElement("templateId");
        templateId.setAttribute("root", "2.16.840.1.113883.10.20.22.2.6.1");
        section.appendChild(templateId);

        Element code = doc.createElement("code");
        code.setAttribute("code", "48765-2");
        code.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
        code.setAttribute("displayName", "Allergies and Adverse Reactions");
        section.appendChild(code);

        Element title = doc.createElement("title");
        title.setTextContent("Allergies");
        section.appendChild(title);

        Element text = doc.createElement("text");
        StringBuilder sb = new StringBuilder();
        for (Allergy a : patient.getAllergies()) {
            sb.append(a.getDescription()).append(" (").append(a.getSeverity()).append("); ");
        }
        if (patient.getAllergies().isEmpty()) sb.append("No known allergies");
        text.setTextContent(sb.toString());
        section.appendChild(text);

        component.appendChild(section);
        return component;
    }

    private Element buildImmunizationsSection(Document doc, Patient patient) {
        Element component = doc.createElement("component");
        Element section = doc.createElement("section");

        Element templateId = doc.createElement("templateId");
        templateId.setAttribute("root", "2.16.840.1.113883.10.20.22.2.2.1");
        section.appendChild(templateId);

        Element code = doc.createElement("code");
        code.setAttribute("code", "11369-6");
        code.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
        code.setAttribute("displayName", "History of immunizations");
        section.appendChild(code);

        Element title = doc.createElement("title");
        title.setTextContent("Immunizations");
        section.appendChild(title);

        Element text = doc.createElement("text");
        StringBuilder sb = new StringBuilder();
        for (Vaccination v : patient.getVaccinations()) {
            sb.append(v.getDescription()).append(" dose ").append(v.getDoseNumber())
              .append(" on ").append(v.getDate()).append("; ");
        }
        text.setTextContent(sb.toString());
        section.appendChild(text);

        component.appendChild(section);
        return component;
    }

    private Element buildResultsSection(Document doc, Patient patient) {
        Element component = doc.createElement("component");
        Element section = doc.createElement("section");

        Element templateId = doc.createElement("templateId");
        templateId.setAttribute("root", "2.16.840.1.113883.10.20.22.2.3.1");
        section.appendChild(templateId);

        Element code = doc.createElement("code");
        code.setAttribute("code", "30954-2");
        code.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
        code.setAttribute("displayName", "Relevant diagnostic tests and/or laboratory data");
        section.appendChild(code);

        Element title = doc.createElement("title");
        title.setTextContent("Results");
        section.appendChild(title);

        Element text = doc.createElement("text");
        StringBuilder sb = new StringBuilder();
        for (LabResult lr : patient.getLabResults()) {
            sb.append(lr.getDescription()).append(": ").append(lr.getValue())
              .append(" ").append(lr.getUnit()).append(" (").append(lr.getInterpretation()).append("); ");
        }
        text.setTextContent(sb.toString());
        section.appendChild(text);

        component.appendChild(section);
        return component;
    }

    private void writeXml(Document doc, File outFile) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        tf.setAttribute(javax.xml.XMLConstants.ACCESS_EXTERNAL_DTD, "");
        tf.setAttribute(javax.xml.XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(outFile);
        transformer.transform(source, result);
    }
}
