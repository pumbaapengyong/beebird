package am.hour.unknown.model;

public class JobShortInfo {
	private String jobName;
	private String likeNumber;
	private String salary;
	private String workPlace;
	private String jobId;
	//�����logo������
	private String companyLogo;
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getLikeNumber() {
		return likeNumber;
	}
	public void setLikeNumber(String likeNumber) {
		this.likeNumber = likeNumber;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public String getWorkPlace() {
		return workPlace;
	}
	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}
	public String getCompanyLogo() {
		return companyLogo;
	}
	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	
	public String toString(){	
		return "���������ǣ�"+jobName+"  �޵������ǣ�"+likeNumber+"  нˮ�ǣ�"+salary+"  �����ص��ǣ�"+workPlace+"  ������ID�ǣ�"+jobId;
	}

}
