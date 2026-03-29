package medicalrecordkeeper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** A patient's physical exam entry, storing vitals and medical data. Srzlb for persistence. */
public class PhysicalExamEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDateTime examDate;         // Exam date and time
    private String chiefComplaint;          // Main symptom or reason for visit
    private String medicalAttendant;        // Healthcare provider's name/ID
    private String bpSystolic;              // Systolic BP (mmHg)
    private String bpDiastolic;             // Diastolic BP (mmHg)
    private String pulseRate;               // Heart rate (bpm)
    private String oxygenSaturation;        // O2 saturation (%)
    private double weight;                  // Weight (kg)
    private double temperature;             // Temperature (°C)
    private String diagnosis;               // Medical diagnosis
    private String treatment;               // Treatment plan

    /** Sets exam date to current time. */
    public PhysicalExamEntry() {
        this.examDate = LocalDateTime.now();
    }

    public LocalDateTime getExamDate() { return examDate; }
    public String getChiefComplaint() { return chiefComplaint; }
    /** Sets chief complaint. */
    public void setChiefComplaint(String chiefComplaint) { this.chiefComplaint = chiefComplaint; }
    public String getMedicalAttendant() { return medicalAttendant; }
    /** Sets medical attendant. */
    public void setMedicalAttendant(String medicalAttendant) { this.medicalAttendant = medicalAttendant; }
    public String getBpSystolic() { return bpSystolic; }
    /** Sets systolic BP. */
    public void setBpSystolic(String bpSystolic) { this.bpSystolic = bpSystolic; }
    public String getBpDiastolic() { return bpDiastolic; }
    /** Sets diastolic BP. */
    public void setBpDiastolic(String bpDiastolic) { this.bpDiastolic = bpDiastolic; }
    public String getPulseRate() { return pulseRate; }
    /** Sets pulse rate. */
    public void setPulseRate(String pulseRate) { this.pulseRate = pulseRate; }
    public String getOxygenSaturation() { return oxygenSaturation; }
    /** Sets O2 saturation. */
    public void setOxygenSaturation(String oxygenSaturation) { this.oxygenSaturation = oxygenSaturation; }
    public double getWeight() { return weight; }
    /** Sets weight. */
    public void setWeight(double weight) { this.weight = weight; }
    public double getTemperature() { return temperature; }
    /** Sets temperature. */
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public String getDiagnosis() { return diagnosis; }
    /** Sets diagnosis. */
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getTreatment() { return treatment; }
    /** Sets treatment. */
    public void setTreatment(String treatment) { this.treatment = treatment; }

    /** Returns exam date as "yyyy-MM-dd HH:mm:ss". */
    public String getFormattedDateTime() {
        return examDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}