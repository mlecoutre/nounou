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
