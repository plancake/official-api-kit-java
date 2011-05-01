/*************************************************************************************
* ===================================================================================*
* Software by: Danyuki Software Limited                                              *
* This file is part of Plancake.                                                     *
*                                                                                    *
* Copyright 2009-2010-2011 by:     Danyuki Software Limited                          *
* Support, News, Updates at:  http://www.plancake.com                                *
* Licensed under the AGPL version 3 license.                                         *                                                       *
* Danyuki Software Limited is registered in England and Wales (Company No. 07554549) *
**************************************************************************************
* Plancake is distributed in the hope that it will be useful,                        *
* but WITHOUT ANY WARRANTY; without even the implied warranty of                     *
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      *
* GNU Affero General Public License for more details.                                *
*                                                                                    *
* You should have received a copy of the GNU Affero General Public License           *
* along with this program.  If not, see <http://www.gnu.org/licenses/>.              *
*                                                                                    *
**************************************************************************************/

import java.util.ArrayList;
import java.util.List;

import com.plancake.api.client.*;

public class Example {

   public static void main(String[] args) {

       try
       {
           PlancakeApiClient apiClient = new PlancakeApiClient("91883a785e05fb087a76419dd826f8570a64288b", // find this API key on your Settings page
                                                            "o3zTZxE0Vds3zqiX", // find the secret on your Settings page
                                                            "http://api.plancake.com/api.php");
           apiClient.userKey = "vah65fZKrwWecbtjh9pg8Za9iBxvXYJb"; // find your user key on your Settings page


           apiClient.extraInfoForGetTokenCall = "This is some info we can send to the server when requesting a token";



            // here is when we can "inject" a token we have cache from the previous requests
            // to avoid to compute it again (it would be just a waste!)
            // apiClient.token = "xxxxxxxxxx";

    	   long fromTimestamp = (System.currentTimeMillis() / 1000L) - (3600*24*2);
    	   long toTimestamp = fromTimestamp + 1000;
    	   
    	   Example.printTitle("Testing getServerTime");
    	   int serverTime = apiClient.getServerTime();
    	   System.out.println("serverTime " + serverTime);
    	   

    	   Example.printTitle("Testing getUserSettings");
    	   PlancakeSettingsForApi settings = apiClient.getUserSettings();
    	   System.out.println("timezoneDescription: " + settings.timezoneDescription);
    	   System.out.println("timezoneOffset: " + settings.timezoneOffset);    	   
    	   System.out.println("timezoneDst: " + settings.timezoneDst);    	
    	   System.out.println("dateFormat: " + settings.dateFormat);
    	   System.out.println("timeFormat: " + settings.timeFormat);
    	   System.out.println("dstActive: " + settings.dstActive);
    	   System.out.println("weekStart: " + settings.weekStart);
    	   System.out.println("lang: " + settings.lang);
    	   System.out.println("clientLangString: " + settings.clientLangString);
    	   
    	    Example.printTitle("Testing getLists");
    	    List<PlancakeListForApi> lists = apiClient.getLists();    
    	    
    	    for (int i = 0; i < lists.size(); i++) {
    	    	System.out.println("listId: " + lists.get(i).id);    	    	
    	    	System.out.println("listName: " + lists.get(i).name);
    	    	System.out.println("listSortOrder: " + lists.get(i).sortOrder);
    	    	System.out.println("listIsInbox: " + lists.get(i).isInbox);
    	    	System.out.println("listIsHeader: " + lists.get(i).isHeader);
    	    	System.out.println("listCreatedAt: " + lists.get(i).createdAt);
    	    	System.out.println("listUpdatedAt: " + lists.get(i).updatedAt);
    	    }

    	    
    	    Example.printTitle("Testing getLists (with timestamp)");
    	    lists = apiClient.getLists(fromTimestamp, toTimestamp);
    	    
    	    for (int i = 0; i < lists.size(); i++) {
    	    	System.out.println("listId: " + lists.get(i).id);    	    	
    	    	System.out.println("listName: " + lists.get(i).name);
    	    	System.out.println("listSortOrder: " + lists.get(i).sortOrder);
    	    	System.out.println("listIsInbox: " + lists.get(i).isInbox);
    	    	System.out.println("listIsHeader: " + lists.get(i).isHeader);
    	    	System.out.println("listCreatedAt: " + lists.get(i).createdAt);
    	    	System.out.println("listUpdatedAt: " + lists.get(i).updatedAt);
    	    }

    	    
    	    Example.printTitle("Testing getDeletedLists");    	    
    	    List<Integer> listIds = apiClient.getDeletedLists(fromTimestamp, toTimestamp);
	    	System.out.println("listIds: ");
    	    for (int i = 0; i < listIds.size(); i++) {
    	    	System.out.print(listIds.get(i) + " - ");
    	    }
	    	System.out.println("");

	    	
	        Example.printTitle("Testing getTags");
    	    List<PlancakeTagForApi> tags = apiClient.getTags();        	    
    	    for (int i = 0; i < tags.size(); i++) {
    	    	System.out.println("tagId: " + tags.get(i).id);    	    	
    	    	System.out.println("tagName: " + tags.get(i).name);
    	    	System.out.println("tagSortOrder: " + tags.get(i).sortOrder);
    	    }  	

    	    
	        Example.printTitle("Testing getTags with timestamp");
    	    tags = apiClient.getTags(fromTimestamp, toTimestamp);
    	    for (int i = 0; i < tags.size(); i++) {
    	    	System.out.println("tagId: " + tags.get(i).id);    	    	
    	    	System.out.println("tagName: " + tags.get(i).name);
    	    	System.out.println("tagSortOrder: " + tags.get(i).sortOrder);
    	    }  	    	    
    	    
    	    
    	    Example.printTitle("Testing getDeletedTags");    	    
    	    List<Integer> tagIds = apiClient.getDeletedTags(fromTimestamp, toTimestamp);
	    	System.out.println("tagIds: ");
    	    for (int i = 0; i < tagIds.size(); i++) {
    	    	System.out.print(tagIds.get(i) + " - ");
    	    }
	    	System.out.println("");    	    

	        Example.printTitle("Testing getRepetitionOptions");
	        List<PlancakeRepetitionOptionForApi> repetitions = apiClient.getRepetitionOptions();
    	    for (int i = 0; i < repetitions.size(); i++) {
    	    	System.out.println("repetitionId: " + repetitions.get(i).id);    	    	
    	    	System.out.println("repetitionLabel: " + repetitions.get(i).label);
    	    	System.out.println("repetitionSpecial: " + repetitions.get(i).special);
    	    	System.out.println("repetitionNeedsParam: " + repetitions.get(i).needsParam);
    	    	System.out.println("repetitionIsParamCardinal: " + repetitions.get(i).isParamCardinal);
    	    	System.out.println("repetitionMinParam: " + repetitions.get(i).minParam);
    	    	System.out.println("repetitionMaxParam: " + repetitions.get(i).maxParam);
    	    	System.out.println("repetitionIcalRruleTemplate: " + repetitions.get(i).icalRruleTemplate);
    	    	System.out.println("repetitionSortOrder: " + repetitions.get(i).sortOrder);
    	    	System.out.println("repetitionHasDividerBelow: " + repetitions.get(i).hasDividerBelow);
    	    }	
    	    
    	    
    	    long validTaskId = 0L;
    	    
    	    
    	    Example.printTitle("Testing getTasks (non-completed)");
	        List<PlancakeTaskForApi> tasks = apiClient.getTasks(new PlancakeTasksFilterOptions());
    	    for (int i = 0; i < tasks.size(); i++) {
    	    	System.out.println("taskId: " + tasks.get(i).id);    	    	
    	    	System.out.println("taskListId: " + tasks.get(i).listId); 
    	    	System.out.println("taskDescription: " + tasks.get(i).description); 
    	    	System.out.println("taskSortOrder: " + tasks.get(i).sortOrder); 
    	    	System.out.println("taskIsStarred: " + tasks.get(i).isStarred);     	    	
    	    	System.out.println("taskIsHeader: " + tasks.get(i).isHeader);    	    	
    	    	System.out.println("taskDueDate: " + tasks.get(i).dueDate); 
    	    	System.out.println("taskDueTime: " + tasks.get(i).dueTime);
    	    	System.out.println("taskRepetitionId: " + tasks.get(i).repetitionId); 
    	    	System.out.println("taskRepetitionParam: " + tasks.get(i).repetitionParam);
    	    	System.out.println("taskRepetitionIcalRrule: " + tasks.get(i).repetitionIcalRrule);
    	    	System.out.println("taskIsCompleted: " + tasks.get(i).isCompleted);     	    	
    	    	System.out.println("taskIsFromSystem: " + tasks.get(i).isFromSystem);    	    	
    	    	System.out.println("taskNote: " + tasks.get(i).note); 
    	    	System.out.println("taskTags: " + tasks.get(i).tagIds); 
    	    	System.out.println("taskCreatedAt: " + tasks.get(i).createdAt); 
    	    	System.out.println("taskUpdatedAt: " + tasks.get(i).updatedAt);     	    	
    	    	
    	    	if (i == 0)
    	    	{
    	    		validTaskId = tasks.get(i).id;
    	    	}
    	    }	

    	    Example.printTitle("TaskId to use later on: " + validTaskId);    


    	    Example.printTitle("Testing getTasks (completed)");
    	    PlancakeTasksFilterOptions filterOptions = new PlancakeTasksFilterOptions();
    	    filterOptions.completed = true;
	        tasks = apiClient.getTasks(filterOptions);
    	    for (int i = 0; i < tasks.size(); i++) {
    	    	System.out.println("taskId: " + tasks.get(i).id);    	    	
    	    	System.out.println("taskListId: " + tasks.get(i).listId); 
    	    	System.out.println("taskDescription: " + tasks.get(i).description); 
    	    	System.out.println("taskSortOrder: " + tasks.get(i).sortOrder); 
    	    	System.out.println("taskIsStarred: " + tasks.get(i).isStarred);     	    	
    	    	System.out.println("taskIsHeader: " + tasks.get(i).isHeader);    	    	
    	    	System.out.println("taskDueDate: " + tasks.get(i).dueDate); 
    	    	System.out.println("taskRepetitionId: " + tasks.get(i).repetitionId); 
    	    	System.out.println("taskRepetitionParam: " + tasks.get(i).repetitionParam);
     	    	System.out.println("taskRepetitionIcalRrule: " + tasks.get(i).repetitionIcalRrule);
    	    	System.out.println("taskIsCompleted: " + tasks.get(i).isCompleted);     	    	
    	    	System.out.println("taskIsFromSystem: " + tasks.get(i).isFromSystem);    	    	
    	    	System.out.println("taskNote: " + tasks.get(i).note); 
    	    	System.out.println("taskTags: " + tasks.get(i).tagIds); 
    	    	System.out.println("taskCreatedAt: " + tasks.get(i).createdAt); 
    	    	System.out.println("taskUpdatedAt: " + tasks.get(i).updatedAt);     	    	
    	    }	    	    

    	    Example.printTitle("Testing getTasks (various criteria)");
    	    filterOptions = new PlancakeTasksFilterOptions();
    	    //filterOptions.listId = 5;
    	    //filterOptions.taskId = 14;
    	    filterOptions.onlyWithDueDate = false;
    	    filterOptions.onlyDueTodayOrTomorrow = false;
    	    filterOptions.onlyStarred = true;

	        tasks = apiClient.getTasks(filterOptions);
    	    for (int i = 0; i < tasks.size(); i++) {
    	    	System.out.println("taskId: " + tasks.get(i).id);    	    	
    	    	System.out.println("taskListId: " + tasks.get(i).listId); 
    	    	System.out.println("taskDescription: " + tasks.get(i).description); 
    	    	System.out.println("taskSortOrder: " + tasks.get(i).sortOrder); 
    	    	System.out.println("taskIsStarred: " + tasks.get(i).isStarred);     	    	
    	    	System.out.println("taskIsHeader: " + tasks.get(i).isHeader);    	    	
    	    	System.out.println("taskDueDate: " + tasks.get(i).dueDate); 
    	    	System.out.println("taskRepetitionId: " + tasks.get(i).repetitionId); 
    	    	System.out.println("taskRepetitionParam: " + tasks.get(i).repetitionParam);
    	    	System.out.println("taskRepetitionIcalRrule: " + tasks.get(i).repetitionIcalRrule);
    	    	System.out.println("taskIsCompleted: " + tasks.get(i).isCompleted);     	    	
    	    	System.out.println("taskIsFromSystem: " + tasks.get(i).isFromSystem);    	    	
    	    	System.out.println("taskNote: " + tasks.get(i).note); 
    	    	System.out.println("taskTags: " + tasks.get(i).tagIds); 
    	    	System.out.println("taskCreatedAt: " + tasks.get(i).createdAt); 
    	    	System.out.println("taskUpdatedAt: " + tasks.get(i).updatedAt);     	    	
    	    }	    	    

    	    
    	    Example.printTitle("Testing getDeletedTasks");    	    
    	    List<Long> taskIds = apiClient.getDeletedTasks(fromTimestamp, toTimestamp);
	    	System.out.println("taskIds: ");
    	    for (int i = 0; i < taskIds.size(); i++) {
    	    	System.out.print(taskIds.get(i) + " - ");
    	    }
	    	System.out.println("");  

	    	
	        Example.printTitle("Testing completeTask");
	        // commented out to avoid unwanted effect
	        // System.out.println("taskId: "  + apiClient.completeTask(validTaskId));

	        Example.printTitle("Testing uncompleteTask");
	        // commented out to avoid unwanted effect
	        // System.out.println("taskId: "  + apiClient.uncompleteTask(validTaskId));	        	        
	        
	        Example.printTitle("Testing setTaskNote");
	        // commented out to avoid unwanted effect	        	        
	        // System.out.println("taskId: "  + apiClient.setTaskNote(35, "This is a note from the Java Api kit"));			        

	        
	        Example.printTitle("Testing addTask");
		    // commented out to avoid unwanted effect	        
	        /*
	        PlancakeTaskForApi task = new PlancakeTaskForApi();
	        task.description = "this is a header from the Java API test";
	        task.isHeader = true;
	        System.out.println("taskId: "  + apiClient.addTask(task));		        
			*/

	        Example.printTitle("Testing addTask (misc parameters)");
		    // commented out to avoid unwanted effect
                /*
	        task = new PlancakeTaskForApi();
	        task.description = "this is a task from the Java API test 2";
	        task.listId = 2;
	        task.dueDate = "2010-12-25";
	        task.dueTime = "1234";
	        task.tagIds = "1,2";
                task.isStarred = true;
	        task.repetitionId = 10;
	        task.repetitionParam = 13;
	        task.note = "this is a simple note fron the API";
	        System.out.println("taskId: "  + apiClient.addTask(task));		              
                */

		    Example.printTitle("Testing editTask");
		    // commented out to avoid unwanted effect
		    /*
		    PlancakeTaskForApi task = new PlancakeTaskForApi();
	        task.description = "this is a task edited by Java API";		    
	        System.out.println("taskId: "  + apiClient.editTask(validTaskId, task));
	        */

		    Example.printTitle("Testing editTask (misc parameters)");
		    // commented out to avoid unwanted effect
		    /*
		    PlancakeTaskForApi task = new PlancakeTaskForApi();
	        task.description = "this is a task edited by Java API 2";
	        task.listId = 2;
	        task.dueDate = "2011-02-07";
	        task.dueTime = "1545";
                task.isStarred = false;
	        task.repetitionId = 12;
	        task.repetitionParam = 19;
	        task.note = "this is a new note from the Java API client";
	        task.tagIds = "2,3";
	        System.out.println("taskId: "  + apiClient.editTask(validTaskId, task));
	        */		    

	        Example.printTitle("Testing deleteTask");
	        // commented out to avoid unwanted effect
	        // System.out.println("taskId: "  + apiClient.deleteTask(validTaskId));

                Example.printTitle("Testing whatHasChanged");
                String[] changedItems = apiClient.whatHasChanged(fromTimestamp, toTimestamp);
                    System.out.println("changedItems: ");
                for (int i = 0; i < changedItems.length; i++) {
                    System.out.print(changedItems[i] + " - ");
                }
                    System.out.println("");

                Example.printTitle("This is the token we have used and we can save for later use");
                System.out.println(apiClient.token);
		    
       }
       catch(Exception e)
       {
    	   System.out.println("ERROR");
    	   System.out.println(e.getMessage());
    	   e.printStackTrace();
       }
   }
   
   private static void printTitle(String title)
   {
	   System.out.println("");
	   System.out.println(">>> " + title + " <<<");
   }

}