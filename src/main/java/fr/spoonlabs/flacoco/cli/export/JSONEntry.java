package fr.spoonlabs.flacoco.cli.export;

public class JSONEntry {

    private String className;
    private Integer lineNumber;
    private Double suspiciousness;

    public JSONEntry(String className, Integer lineNumber, Double suspiciousness) {
        this.className = className;
        this.lineNumber = lineNumber;
        this.suspiciousness = suspiciousness;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Double getSuspiciousness() {
        return suspiciousness;
    }

    public void setSuspiciousness(Double suspiciousness) {
        this.suspiciousness = suspiciousness;
    }
}
