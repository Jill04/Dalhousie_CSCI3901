import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class CommitTest {

	public static void main(String[] args) {
		CommitManager commit = new CommitManager();
		Set<String> files = new HashSet<String>();
		files.add("A");
		files.add("B");
		files.add("C");
		
		
		Set<String> file1 = new HashSet<String>();
		file1.add("C");
		file1.add("B");
		file1.add("D");
		
		Set<String> file2 = new HashSet<String>();
		file2.add("A");
		file2.add("D");
		
		Set<String> file3 = new HashSet<String>();
		file3.add("E");
		file3.add("C");
		
		commit.setTimeWindow(21, 1611);
		commit.addCommit("Jill", (LocalDateTime.now().getHour() * 60)+LocalDateTime.now().getMinute(), "B-67", files);
		commit.addCommit("Yung", (LocalDateTime.now().getHour() * 60)+LocalDateTime.now().getMinute(), "F-657", file1);
	    commit.addCommit("Jill", (LocalDateTime.now().getHour() * 60)+LocalDateTime.now().getMinute(), "B-67", file2);
	    commit.clearTimeWindow();
	    commit.addCommit("Medieth", (LocalDateTime.now().getHour() * 60)+LocalDateTime.now().getMinute(), "B-867", file3);

	    commit.setTimeWindow(21, 1611);
		
	    commit.addCommit("Medieth", (LocalDateTime.now().getHour() * 60)+LocalDateTime.now().getMinute(), "B-867", file3);

	    System.out.println(commit.repetitionInBugs(2));
	    System.out.println(commit.busyClasses(3));
		System.out.println(commit.componentMinimum(5));
		System.out.println(commit.componentMinimum(2));
		System.out.println(commit.softwareComponents());
		System.out.println(commit.broadFeatures(2));
		System.out.println(commit.experts(2));
	}
}
