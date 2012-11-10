package org.mat.nounou.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * User: mlecoutre
 * Date: 31/10/12
 * Time: 17:00
 */
@XmlRootElement
public class ReportVO {
    private String reportTitle;
    private String totalDuration;
    private List<AppointmentVO> appointments;
    private List<Double[]> data;
    private List<Double[]> dataRange;

    public List<Double[]> getData() {
        return data;
    }


    public List<Double[]> getDataRange() {
        return dataRange;
    }

    public void setDataRange(List<Double[]> dataRange) {
        this.dataRange = dataRange;
    }

    public void setData(List<Double[]> data) {
        this.data = data;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public List<AppointmentVO> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentVO> appointments) {
        this.appointments = appointments;
    }


}
