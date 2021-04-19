public class Student{
	private long seatNumber;
        private double average;
	private String branch;

	public Student(){}
	
	public Student(long seatNumber, String branch, double average){
	   this.seatNumber = seatNumber;
	   this.average = average;
	   this.branch = branch;
	}

	//accessors
	public long getSeatNumber(){return this.seatNumber;}
	public void setSeatNumber(long seatNumber){this.seatNumber = seatNumber;}

	public double getAverage(){return this.average;}
	public void setAverage(double average){this.average = average;}

	public String getBranch(){return this.branch;}
	public void setBranch(String branch){this.branch = branch;}
}