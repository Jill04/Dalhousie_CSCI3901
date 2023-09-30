import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//Stores teh Commit details
public class CommitDetails {
    String developerName;
    int commitTime;
    String task;
    Set<String> filenames;
  
    CommitDetails(String developerName, int commitTime, String task, Set<String> filenames){
	  this.developerName = developerName;
	  this.commitTime = commitTime;
	  this.task = task;
	  this.filenames = filenames;	
    }
    
    //Getter and setter method of each variable
	public String getDeveloperName() {
		return developerName;
	}
	
	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}
	
	public int getCommitTime() {
		return commitTime;
	}
	
	public void setCommitTime(int commitTime) {
		this.commitTime = commitTime;
	}
	
	public String getTask() {
		return task;
	}
	
	public void setTask(String task) {
		this.task = task;
	}
	
	public Set<String> getFilenames() {
		return filenames;
	}
	
	public void setFilenames(Set<String> filenames) {
		this.filenames = filenames;
	}
  
	
}
