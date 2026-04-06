package com.synthtwin.exporter;

import com.synthtwin.model.Patient;

import java.io.IOException;
import java.util.List;

public interface Exporter {
    void export(List<Patient> patients, String outputDir) throws IOException;
}
