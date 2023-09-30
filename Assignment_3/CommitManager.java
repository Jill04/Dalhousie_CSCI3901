import static java.util.stream.Collectors.toMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class CommitManager {
	
 //Stores the window start time
 int windowStartTime;
 //Stores the window end time
 int windowEndTime;
 //Stores the details of all commits
 List<CommitDetails> commit = new ArrayList<>();
 //Stores the information of all tasks and keeps a count
 Map<String, HashMap <String,Integer>> taskCommitCount = new HashMap<String,HashMap<String,Integer>>() ;
 //Stores the information about the files that have appeared together in the commit
 Map<String,Integer> combinations = new HashMap<String,Integer>();
 //Current component list
 Set<Set<String>> components = new HashSet<Set<String>>();
 
 CommitManager(){}
 
 /*
  * Sets the time slot for adding commits
  * 
  * @param  startTime  start time of the window
  * @param  endTime    end time of the window
  */
 public boolean setTimeWindow(int startTime, int endTime) {
	 boolean isSet = false;
	 if(startTime <= 0 || endTime <= 0) {
		 System.out.println("Error: Start time or end time is zero");
		 return  isSet;
	 }
	 //Checks if current window time has be cleared before setting new one
	 if(this.windowEndTime ==0 && this.windowStartTime ==0 ) {
		 //Test for valid window time
		 if(isValidWindowTime(startTime,endTime)) {
			 this.windowStartTime = startTime;
			 this.windowEndTime  = endTime;
			 return isSet = true; 
		 }
	 }
	 return isSet; 
 }
 
 /*
  * Clears current window time
  * 
  */
 public void clearTimeWindow() {
	 //Can only be cleared of not equal to zero
	 if(this.windowEndTime !=0 && this.windowStartTime !=0 ) {
		 this.windowStartTime = 0;
		 this.windowEndTime = 0;
		
	 }
	 else{
		System.out.println("Error: Cannot clear window time"); 
	 } 
 }
 
 /*
  * Adds the commit details 
  * 
  * @param  developer   Specifies the name of the developer
  * @param  commitTime  Current time during the commit
  * @param  task        Type of the task committed
  * @param  commitFiles Stores the files that are committed
  */
 public void addCommit(String developer, int commitTime, String task, Set<String> commitFiles) throws IllegalArgumentException{
	//Check for preconditions before moving forward
	 if(preConditionsCheck(developer,commitTime,task,commitFiles)) {
		//Can only commit if the window slot is open
		 if(isWindowOpen()) {
			CommitDetails commitDetails = new CommitDetails(developer,commitTime,task,commitFiles);
			//Stores the commit details
			this.commit.add(commitDetails);
			//Stores the task details
			  addTaskCommit(task,commitFiles);
			  if(commitFiles.size()>1)
				 {
				  String list = String.join("", commitFiles);
				  int chunkSize = 2;
				   // Stores the combinations of the files appeared togther
				    Store(combination(chunkSize,list));
				   }	
		       }
		   }
      }
 
 /*
  * To find bug files with specified threshold
  * 
  * @param   threshold  threshold value 
  * @return             Set of filename that meet the threshold
  */
 public Set<String> repetitionInBugs(int threshold){
	 if(threshold <= 0) {
		 System.out.println("Error: Threshold cannot be zero or negative");
	 }
	 Set<String> finalSet = new HashSet<String>();
	 Iterator<Map.Entry<String, HashMap<String, Integer>>> tasks = taskCommitCount.entrySet().iterator();
	 while(tasks.hasNext()) {
		 Map.Entry<String, HashMap<String, Integer>> bugs = tasks.next();
		 if(bugs.getKey().startsWith("B")) {
			 Iterator<Map.Entry<String, Integer>> bugIterator = (bugs.getValue()).entrySet().iterator();
			 while(bugIterator.hasNext()) {
				Entry<String, Integer> files = bugIterator.next() ;
				Integer count = files.getValue();
				if(count >= threshold) {
					finalSet.add(bugs.getKey());
				}
			} 
		 } 
	 }
	 return finalSet;
 }
 
 /*
  * To find the busy classes up to the limit in descending order
  * 
  * @param  limit  number of classes to be returned
  */
 public List<String> busyClasses(int limit){
	 List<String> finalList = new ArrayList<String>();
	 if(limit <= 0) {
		 System.out.println("Error: Limit cannot be zero or negative");
	 }
	 
	 Iterator<Map.Entry<String, HashMap<String, Integer>>> tasks = taskCommitCount.entrySet().iterator();
	 Map<String, Integer> fileList = new HashMap<String, Integer>(); 
	 while(tasks.hasNext()) {
		 Map.Entry<String, HashMap<String, Integer>> values = tasks.next();
		 Iterator<Map.Entry<String, Integer>> iterator = (values.getValue()).entrySet().iterator();
			 while(iterator.hasNext()) {
				Entry<String, Integer> files = iterator.next() ;
				String file = files.getKey();
				Integer count = files.getValue();
				if(fileList.containsKey(file)) {
					Integer prevCount = fileList.get(file);
					fileList.put(file, count+prevCount);	    
			    } 
				else {
					fileList.put(file, count);
		        } 
	          }
	        }
	 finalList = sortList(fileList,limit);
	 return finalList;
 }
 /*
  * Find the minimum components with specified threshold
  */
 public boolean componentMinimum(int threshold) {
	 if(threshold <= 0) {
		 System.out.println("Error: Threshold cannot be zero or negative");
		 return false;
	 }
	 if(isWindowOpen()) {
		if(!generateComponents(threshold).isEmpty()) {
	    	this.components = generateComponents(threshold);
		     return true;
	    	}
	  }
	 return false;     
 }
 
 /*
  * To retrieve set of components 
  */
 public Set<Set<String>> softwareComponents (){
	if(isWindowOpen()) {
	  if(!this.components.isEmpty()) {
    	 return this.components;
    	}
	 }
     return null;
  }
 
 /*
  * Returns the feature commit files from specified threshold 
  */
 public Set<String> broadFeatures (int threshold ){
	 Set<String> finalSet = new HashSet<String>();
	 if(threshold < 0) {
		 System.out.println("Error: Threshold cannot be empty");
		 return finalSet;
	 }
	 if(isWindowOpen()) {
		 if(componentMinimum(threshold)) {
	     Set<Set<String>> componentsList =  softwareComponents();
		 Iterator<Set<String>> tasks = componentsList.iterator();
		 while(tasks.hasNext()) {
			 Set<String> list = tasks.next();
			 Iterator<String> files = list.iterator();
			 while(files.hasNext()) {
				 String component=files.next();
			     for(CommitDetails cd : commit) { 
			      if(cd.getFilenames().contains(component)) {
			    	 if(cd.getTask().startsWith("F"))
					 { 
						 finalSet.add(cd.getTask());
					 }
				  }
			    }
		      }
		    }
		 }
	   }
  return finalSet;
		
 }
 /*
  * Returns the experts list from specified threshold 
  */
 public Set<String> experts(int threshold){ 
	 Set<String> finalSet = new HashSet<String>();
	 if(threshold < 0) {
		 System.out.println("Error: Threshold cannot be empty");
		 return finalSet;
	 }
	 if(isWindowOpen()) {
		 if(componentMinimum(threshold)) {
		 Set<Set<String>> componentsList =  softwareComponents();
		 Iterator<Set<String>> tasks = componentsList.iterator();
		 while(tasks.hasNext()) {
			 Set<String> list = tasks.next();
			 Iterator<String> files = list.iterator();
			 while(files.hasNext()) {
				 String component=files.next();
			     for(CommitDetails cd : commit) {
			        if(cd.getFilenames().contains(component)) {
					 { 
						 finalSet.add(cd.getDeveloperName());
				     }
			       }
			    }	 
		      }
	        }
		 }
	   }
     return finalSet;

 }
 
 /********** Helper Functions ************/
 //Checks for the null values
 private boolean notNull(Object obj) {
	if(obj != null) {
		return true;
	}
	System.out.println(" Error: Empty or Null Input");
	return false;
  }
 
 //Determines whether the window time is valid or not
 private boolean isValidWindowTime(int startTime, int endTime) {
	 
		if(endTime < startTime) {
			System.out.println(" Error: End time cannot be less than start time");
			return false;
		}
		int currentTime = (LocalDateTime.now().getHour() * 60)+ LocalDateTime.now().getMinute();
		if( currentTime >= endTime) {
			System.out.println(" Error: Not a valid start time or end time");
			return false;
		}
		return true;
	  }
 
 //Precondition check for adding commits
 private boolean preConditionsCheck(String developer, int commitTime, String task, Set<String> commitFiles) {
	 if(notNull(developer) && !developer.isBlank() && notNull(task) && !task.isBlank() ) {
		 if(task.charAt(0) == 'B' || task.charAt(0) == 'F' && task.charAt(1) =='-' )
		 {
			String valid = task.substring(2, task.length());
			if(valid.chars().allMatch(Character::isDigit)) {
			 if(commitTime > 0 && !commitFiles.isEmpty() ) {
				 return true;
			 }
		  }
		}
	 }
	 return false;
 }
 
 //Checks if window id open or not
 private boolean isWindowOpen() {
	 int currentTime =  (LocalDateTime.now().getHour() * 60)+ LocalDateTime.now().getMinute();
	 if(currentTime >= this.windowStartTime && currentTime <= this.windowEndTime) {
		 return true;
	 }
	 return false;
 }
 
 //Adds the task files
 private void addTaskCommit(String task, Set<String> filenames) {
	        HashMap<String, Integer> fileCount = new HashMap<String,Integer>();
	        
	        //Checks for empty map
	 		if(taskCommitCount.isEmpty()) {
	 			Iterator<String> it = filenames.iterator();
				
			     while (it.hasNext()) {
					fileCount.put(it.next(),1);	
				}
			    taskCommitCount.put(task,fileCount);	 		
	 		}
	 		
	 		//Increments if the task appears one or more number of times
	 		else if(taskCommitCount.containsKey(task) )
	 		{
	 			fileCount = new HashMap<String,Integer>();
	 			fileCount = taskCommitCount.get(task);
	 			Iterator<String> it = filenames.iterator();
				
			     while (it.hasNext()) {
			     String file = it.next();
			     if(fileCount.containsKey(file)) {
			    	 //Gets the current task count
			    	 Integer count = fileCount.get(file);
		 			 fileCount.put(file, ++count);	
			     }
			     else {
			    	 fileCount.put(file, 1);
			    	}
			    }
			     taskCommitCount.put(task,fileCount);
	 		}
	 		else {
	 			Iterator<String> it = filenames.iterator();
				
			     while (it.hasNext()) {
			    	 String file = it.next();
					fileCount.put(file,1);
				 }
			    taskCommitCount.put(task,fileCount);
	 		}
	 	}
 
 //Sorts the map up to the specified limit 
 public List<String> sortList(Map<String, Integer> map, int limit) {
	   List<String> list = new ArrayList<>();
     
      map = map
    	        .entrySet()
    	        .stream()
    	        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
    	        .collect(
    	            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
    	                LinkedHashMap::new));
            Set<String> keys = map.keySet();
      String[] array = keys.toArray(new String[keys.size()]);
      for(int i =0; i<=limit-1 ;i++) {
    	  list.add(array[i]);
        }
      
      for(int i = limit-1; i< array.length-1 ; i++) {
    	  if(map.get(array[i]) == map.get(array[i+1]) )
    	  {
    		  list.add(array[i+1]);
    	  }
       } 
        return list;
      }
 
 //Stores the combinations of files
 private  void Store(List<String> list) {
		
		Iterator<String> itr = list.iterator();
		while(itr.hasNext()) {
			String value = itr.next();
			if(combinations.isEmpty()) {
				combinations.put(value, 1);
			}
			//Increments the count if the combination of file exists 		
			else if(combinations.containsKey(value) || combinations.containsKey(String.valueOf(value.charAt(1)).concat(String.valueOf(value.charAt(0))))){
			  Integer count = combinations.get(value);
					combinations.put(value, ++count); 	
			}
			else {
				combinations.put(value, 1);
			}
		}
	}
 
 //Generates the combination of specified length
 private  List<String> combination(int i, String input) {
		  
		    List<String> result = new ArrayList<String>();
		    if (i == 0)
		        return result;

		    String first = input.substring(0, i);
		    result.add(first);

		    if (input.length() == i) {
		        return result;
		    }

		    // Recursively find substrings of next smaller length not including the first character
		    List<String> tails = combination(i-1, input.substring(1));

		    // Append first char to each result of the recursive call.
		    for (String sub: tails) {
		        String s = input.substring(0, 1) + sub;
		        if (!(result.contains(s)))
		            result.add(s);
		    }

		    // Add all substring of current length not including first character
		    result.addAll(combination(i, input.substring(1)));
		    return result;
		   }

 //Tests if an edge exists between the nodes
 private Set<String> testEdge(Set<String> set , String vertice) {
    	   Set<String> edges = new HashSet<String>();
    	   Iterator<String> itr = set.iterator();
    	   while(itr.hasNext()) {
    		    String value = itr.next();
    		    if(combinations.containsKey(value.concat(vertice)) || combinations.containsKey(vertice.concat(value))) {
    		    	edges.add(value);
    		    	edges.add(vertice);
    		   }
    	   }
    	   return edges;
       }
 
 // Generates the software components on basis of threshold
 private Set<Set<String>> generateComponents(int threshold){
    	   Set<Set<String>>collect = new HashSet<Set<String>>();
    		  Set<String> set1 = new HashSet<String>();
    		  Set<String> set2 = new HashSet<String>();
    	      Set<String> set3 = new HashSet<String>();
    		  
    	      //Stores the component that meet the threshold
    		  for (Map.Entry<String,Integer> entry : combinations.entrySet()) {
    			          if(entry.getValue() >= threshold) {
    	                	  set1.add(String.valueOf(entry.getKey().charAt(0)));
    	                	  set1.add(String.valueOf(entry.getKey().charAt(1)));
    	                  }
    		  }
    		  if(!set1.isEmpty())
    		  {
    			  collect.add(set1);
    			  for (Map.Entry<String,Integer> entry : combinations.entrySet()) {
        	          
    	        	  set3.add(String.valueOf(entry.getKey().charAt(0)));
    	        	  set3.add(String.valueOf(entry.getKey().charAt(1)));
    	          }
    		  set3.removeAll(set1);
    		 //Stores the component in different set whose threshold requirement is not met
	    	  Iterator<String> it = set3.iterator();   
    		    while(it.hasNext()) {
    	    	  String value = it.next();
    	    	  Set<String> data = testEdge(set3,value);
    	    	  if(!data.isEmpty()) {
    	    		  collect.add(data);
    	    		  set2.add(value);
    	    	  }  	    	 
    		  }
    		  
    		  set3.removeAll(set2);
    		//Stores the component in different set whose threshold requirement is not met
    		  Iterator<String> itr = set3.iterator();   
        		  while(itr.hasNext()) {
        			Set<String> data = new HashSet<String>();
        		    data.add(itr.next());
        			collect.add(data);
        		}
    		  }
            return collect;
	    }
   }
