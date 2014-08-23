package am.hour.beebird.model;

public class JobInfo {
	
	private String jobId;
	private String jobName ;
    private String salaryType;
    private String salary;
    private String workPlace;
    private String highLights;
    private String companyId;
    private String companyName;
    private String companyLogo;
    private String jobPic;
    private String picIntroduce;
    private String weHave;
    private String weHope;
    private String jobDescripCoord;
    private String jobLink;
    
    
    public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
    
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getSalaryType() {
		return salaryType;
	}
	public void setSalaryType(String salaryType) {
		this.salaryType = salaryType;
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
	public String getHighLights() {
		return highLights;
	}
	public void setHighLights(String highLights) {
		this.highLights = highLights;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyLogo() {
		return companyLogo;
	}
	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}
	public String getJobPic() {
		return jobPic;
	}
	public void setJobPic(String jobPic) {
		this.jobPic = jobPic;
	}
	public String getPicIntroduce() {
		return picIntroduce;
	}
	public void setPicIntroduce(String picIntroduce) {
		this.picIntroduce = picIntroduce;
	}
	public String getWeHave() {
		return weHave;
	}
	public void setWeHave(String weHave) {
		this.weHave = weHave;
	}
	public String getWeHope() {
		return weHope;
	}
	public void setWeHope(String weHope) {
		this.weHope = weHope;
	}
	public String getJobDescripCoord() {
		return jobDescripCoord;
	}
	public void setJobDescripCoord(String jobDescripCoord) {
		this.jobDescripCoord = jobDescripCoord;
	}
	public String getJobLink() {
		return jobLink;
	}
	public void setJobLink(String jobLink) {
		this.jobLink = jobLink;
	}
    public String toString(){
		return "工作名称是："+jobName+"公司名称是："+companyName;
    	
    }

}