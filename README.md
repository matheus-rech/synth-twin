# SynthTwin — Synthetic Patient Data Generator

A command-line Java tool that simulates a population of realistic (but entirely fake) patients, generating comprehensive health histories from birth to death.

## Features

- **Full life simulation** — patients are generated with birth dates, death dates, and a complete chronological health timeline
- **Rich health records** — primary care visits, emergency encounters, chronic and acute conditions, medications, allergies, vaccinations, and lab results
- **Realistic demographics** — randomised US names, addresses, race, and ethnicity across 10 states
- **Multiple export formats** — FHIR R4 (JSON bundles), C-CDA (HL7 CDA R2 XML), and CSV
- **Reproducible output** — set `--seed` to get identical datasets across runs
- **Flexible filtering** — restrict by population size, state, city, age range, and gender

## Requirements

- Java 17+
- Apache Maven 3.6+ (for building)

## Build

```bash
mvn package -DskipTests
```

This produces a self-contained fat jar at `target/synth-twin-1.0.0-jar-with-dependencies.jar`.

## Usage

```
java -jar target/synth-twin-1.0.0-jar-with-dependencies.jar [OPTIONS]
```

### Options

| Flag | Long form | Default | Description |
|------|-----------|---------|-------------|
| `-p` | `--population` | `100` | Number of patients to generate |
| `-s` | `--state` | *(any)* | Two-letter US state abbreviation (e.g. `MA`) |
| `-c` | `--city` | *(any)* | City name |
| | `--min-age` | `0` | Minimum patient age |
| | `--max-age` | `110` | Maximum patient age |
| | `--gender` | *(both)* | `M` or `F` |
| `-o` | `--output` | `output` | Output directory |
| `-f` | `--format` | `fhir,ccda,csv` | Comma-separated list of formats |
| | `--seed` | *(random)* | Random seed for reproducibility |
| `-h` | `--help` | | Print help and exit |

### Examples

```bash
# Generate 200 patients in Massachusetts, all formats
java -jar target/synth-twin-1.0.0-jar-with-dependencies.jar -p 200 -s MA -o ./data

# Generate 50 female patients aged 30-60, CSV only
java -jar target/synth-twin-1.0.0-jar-with-dependencies.jar -p 50 --gender F --min-age 30 --max-age 60 -f csv -o ./female-data

# Reproducible run with a fixed seed
java -jar target/synth-twin-1.0.0-jar-with-dependencies.jar -p 100 --seed 12345 -o ./reproducible
```

## Output Structure

```
output/
├── fhir/
│   ├── <patient-uuid>.json    # FHIR R4 Bundle per patient
│   └── bundle_summary.json    # Index of all patient IDs
├── ccda/
│   └── <patient-uuid>.xml     # C-CDA document per patient
└── csv/
    ├── patients.csv
    ├── encounters.csv
    ├── conditions.csv
    ├── medications.csv
    ├── allergies.csv
    ├── vaccinations.csv
    └── lab_results.csv
```

### FHIR R4 Bundle contents

Each JSON file is a FHIR `collection` Bundle containing:
`Patient` · `Encounter` · `Condition` · `MedicationRequest` · `AllergyIntolerance` · `Immunization` · `Observation`

### C-CDA sections

Each XML document includes: Problems · Medications · Allergies · Immunizations · Results

### CSV columns

| File | Key columns |
|------|-------------|
| `patients.csv` | id, first\_name, last\_name, birth\_date, death\_date, gender, address\_\*, race, ethnicity |
| `encounters.csv` | id, patient\_id, type, date, reason\_code, reason\_description, provider\_name, cost, duration\_minutes |
| `conditions.csv` | id, patient\_id, code, description, category, onset\_date, abatement\_date, clinical\_status |
| `medications.csv` | id, patient\_id, code, description, start\_date, stop\_date, reason\_code, reason\_description, dosage\_instructions |
| `allergies.csv` | id, patient\_id, code, description, type, category, severity, onset\_date, reaction |
| `vaccinations.csv` | id, patient\_id, cvx\_code, description, date, dose\_number, series |
| `lab\_results.csv` | id, patient\_id, encounter\_id, loinc\_code, description, value, unit, date, reference\_range\_\*, interpretation |

## Running Tests

```bash
mvn test
```

## Project Structure

```
src/
├── main/java/com/synthtwin/
│   ├── Main.java                  # CLI entry point
│   ├── cli/Options.java           # Argument parsing (Commons CLI)
│   ├── data/
│   │   ├── DemographicData.java   # Names, states, cities
│   │   └── MedicalData.java       # Conditions, meds, vaccines, labs
│   ├── generator/
│   │   └── PatientGenerator.java  # Life simulation engine
│   ├── model/                     # Patient, Encounter, Condition, …
│   └── exporter/
│       ├── FhirExporter.java      # FHIR R4 JSON
│       ├── CcdaExporter.java      # C-CDA XML
│       └── CsvExporter.java       # 7 CSV files
└── test/java/com/synthtwin/
    ├── generator/PatientGeneratorTest.java
    └── exporter/{Fhir,Ccda,Csv}ExporterTest.java
```
