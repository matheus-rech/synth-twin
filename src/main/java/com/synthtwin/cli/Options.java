package com.synthtwin.cli;

import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Thrown when the user passes {@code -h/--help}.
 */
class HelpRequestedException extends RuntimeException {
    HelpRequestedException() { super("Help requested"); }
}

public class Options {
    private int population;
    private String state;
    private String city;
    private int minAge;
    private int maxAge;
    private String gender;
    private String outputDir;
    private Set<String> formats;
    private long seed;

    public Options() {
        this.population = 100;
        this.minAge = 0;
        this.maxAge = 110;
        this.outputDir = "output";
        this.formats = new HashSet<>(Arrays.asList("fhir", "ccda", "csv"));
        this.seed = System.currentTimeMillis();
    }

    /**
     * Parse CLI arguments into an {@link Options} instance.
     *
     * @throws IllegalArgumentException if the arguments cannot be parsed
     * @throws HelpRequestedException   if {@code -h/--help} was passed
     */
    public static Options parse(String[] args) {
        org.apache.commons.cli.Options cliOptions = buildCliOptions();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(cliOptions, args);

            if (cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("synth-twin", cliOptions);
                throw new HelpRequestedException();
            }

            Options options = new Options();

            if (cmd.hasOption("p")) {
                options.population = Integer.parseInt(cmd.getOptionValue("p"));
            }
            if (cmd.hasOption("s")) {
                options.state = cmd.getOptionValue("s");
            }
            if (cmd.hasOption("c")) {
                options.city = cmd.getOptionValue("c");
            }
            if (cmd.hasOption("min-age")) {
                options.minAge = Integer.parseInt(cmd.getOptionValue("min-age"));
            }
            if (cmd.hasOption("max-age")) {
                options.maxAge = Integer.parseInt(cmd.getOptionValue("max-age"));
            }
            if (cmd.hasOption("gender")) {
                options.gender = cmd.getOptionValue("gender");
            }
            if (cmd.hasOption("o")) {
                options.outputDir = cmd.getOptionValue("o");
            }
            if (cmd.hasOption("f")) {
                String formatsStr = cmd.getOptionValue("f");
                options.formats = new HashSet<>(Arrays.asList(formatsStr.split(",")));
            }
            if (cmd.hasOption("seed")) {
                options.seed = Long.parseLong(cmd.getOptionValue("seed"));
            }

            return options;

        } catch (ParseException e) {
            throw new IllegalArgumentException("Error parsing arguments: " + e.getMessage(), e);
        }
    }

    private static org.apache.commons.cli.Options buildCliOptions() {
        org.apache.commons.cli.Options cliOptions = new org.apache.commons.cli.Options();

        cliOptions.addOption(Option.builder("p").longOpt("population").hasArg().desc("Number of patients (default: 100)").build());
        cliOptions.addOption(Option.builder("s").longOpt("state").hasArg().desc("State abbreviation (e.g., MA)").build());
        cliOptions.addOption(Option.builder("c").longOpt("city").hasArg().desc("City name").build());
        cliOptions.addOption(Option.builder().longOpt("min-age").hasArg().desc("Minimum patient age (default: 0)").build());
        cliOptions.addOption(Option.builder().longOpt("max-age").hasArg().desc("Maximum patient age (default: 110)").build());
        cliOptions.addOption(Option.builder().longOpt("gender").hasArg().desc("Gender filter: M or F").build());
        cliOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output directory (default: output)").build());
        cliOptions.addOption(Option.builder("f").longOpt("format").hasArg().desc("Output formats: fhir,ccda,csv (default: all)").build());
        cliOptions.addOption(Option.builder().longOpt("seed").hasArg().desc("Random seed for reproducibility").build());
        cliOptions.addOption(Option.builder("h").longOpt("help").desc("Print help").build());

        return cliOptions;
    }

    public int getPopulation() { return population; }
    public void setPopulation(int population) { this.population = population; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public int getMinAge() { return minAge; }
    public void setMinAge(int minAge) { this.minAge = minAge; }
    public int getMaxAge() { return maxAge; }
    public void setMaxAge(int maxAge) { this.maxAge = maxAge; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getOutputDir() { return outputDir; }
    public void setOutputDir(String outputDir) { this.outputDir = outputDir; }
    public Set<String> getFormats() { return formats; }
    public void setFormats(Set<String> formats) { this.formats = formats; }
    public long getSeed() { return seed; }
    public void setSeed(long seed) { this.seed = seed; }
}
