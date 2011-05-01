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

package com.plancake.api.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.security.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.io.IOException;


public class PlancakeApiClient {

    private static final int API_VERSION = 3;
    private static final int INVALID_TOKEN_ERROR = 30; 	
	
    private String apiKey = null;
    private String apiSecret = null;
    private String apiEndpointUrl = null;

    public String userKey = null;
    private String emailAddress = null;
    private String password = null;
    public String token = "";
    public String extraInfoForGetTokenCall = "";

    /**
    *
    * @param string apiKey - i.e.: rei93454jherER5439utkerj43534ter
    * @param string apiSecret - i.e.: t4r95FDS4Erjt3lk
    * @param string apiEndpointUrl - i.e.: http://www.plancake/api.php
    */
    public PlancakeApiClient(String apiKey, String apiSecret, String apiEndpointUrl)
    {
    	this(apiKey, apiSecret, apiEndpointUrl, null, null);
    }
    
    /**
    *
    * @param string apiKey - i.e.: rei93454jherER5439utkerj43534ter
    * @param string apiSecret - i.e.: t4r95FDS4Erjt3lk
    * @param string apiEndpointUrl - i.e.: http://www.plancake/api.php
    * @param string emailAddress - used rarely, not applicable to personal API keys
    * @param string password - used rarely, not applicable to personal API keys
    */
    public PlancakeApiClient(String apiKey, String apiSecret, String apiEndpointUrl, String emailAddress, String password)
    {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.apiEndpointUrl = apiEndpointUrl;

        if (emailAddress != null)
        {
            this.emailAddress = emailAddress;
        }

        if (password != null)
        {
            this.password = password;
        }
    }   
    
    /**
    *
    * @param array params - including token
    * @param string $methodName
    * @return string
    */
   private String getSignatureForRequest(Map<String, String> params, String methodName) 
   			throws UnsupportedEncodingException, NoSuchAlgorithmException
   {   
	    SortedSet<String> sortedSet= new TreeSet<String>(params.keySet());
	
	    Iterator<String> it = sortedSet.iterator();   
	    
		String s = methodName;
		
	    while (it.hasNext()) {
	      String paramKey = it.next();
	      String paramValue = (String)params.get(paramKey);
	      s += paramKey + paramValue;
	    }
	    
	    s += this.apiSecret;

	    return Utils.md5(s);
   }    
   
   /**
   *
   * @param Map params
   * @param string methodName
   * @return string
   */
  private String prepareRequest(Map<String, String> params, String methodName) 
  			throws UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException
  {
	  params.put("token", this.token);
        params.put("api_ver", Integer.toString(PlancakeApiClient.API_VERSION));

      String signature = this.getSignatureForRequest(params, methodName);

      String request = this.apiEndpointUrl + '/' + methodName + "/?";
      
      Set<Map.Entry<String, String>> paramsSet = params.entrySet();

      Iterator it = paramsSet.iterator();

      for (Map.Entry<String, String> me : paramsSet) {
  	  	request += me.getKey() + '=' + URLEncoder.encode(me.getValue(), "UTF-8") + '&';        
      }      

      request += "sig=" + signature;
      
      // this should do URL-encoding of the request
      URI uri = new URI(request);
      
      return uri.toString();
  }

  /**
   * @param String request
   * @return String
   */
  private String getResponse(String request) throws MalformedURLException, IOException, PlancakeApiException
  {
	  return this.getResponse(request, "GET");
  }
  
  /**
   * @param String request
   * @param String httpMethod
   * @return String
   */
  private String getResponse(String request, String httpMethod) throws MalformedURLException, IOException, PlancakeApiException
  {
	 if ( !(httpMethod.equals("GET")) && !(httpMethod.equals("POST")) )
	 {
		 throw new PlancakeApiException("httpMethod must be either GET or POST");
	 }
	 
	 String urlParameters = "";
	 
	  if (httpMethod == "POST")
	  {
		  String[] requestParts = request.split("\\?");
		  request = requestParts[0];
		  urlParameters = requestParts[1];
	  }
	 
      URL url = new URL(request); 
      
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();           
      connection.setDoOutput(true); 
      connection.setDoInput(true);      
      connection.setInstanceFollowRedirects(false); 
      connection.setRequestMethod(httpMethod); 
      if (httpMethod == "POST")
      {      
    	  connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
      }
      else
      {
    	  connection.setRequestProperty("Content-Type", "text/plain");    	  
      }
      connection.setRequestProperty("charset", "utf-8");
      connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));      
      connection.setUseCaches (false);      
      connection.connect();      

      if (httpMethod == "POST")
      {
	      DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
	      wr.writeBytes(urlParameters);
	      wr.flush();
	      wr.close();     
      }   
            
      StringBuffer text = new StringBuffer();
      InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
      BufferedReader buff = new BufferedReader(in);
      String line = buff.readLine();
      while (line != null) {
          text.append(line + "\n");
          line = buff.readLine();
      }
      String response = text.toString();      

      if (response.length() == 0)
      {
    	  throw new PlancakeApiException("the response is empty");
      }
      
      connection.disconnect();
      return response;
  }

  private void resetToken() throws UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, RuntimeException, PlancakeApiException
  {
	  // we don't have a token yet or it has been reset as
	  // it was probably expired
	  this.token = "";

	  Map<String, String> params = new HashMap<String, String>();
	  params.put("token", "");
	  params.put("api_key", this.apiKey);
	  params.put("api_ver", Integer.toString(PlancakeApiClient.API_VERSION));

          if (this.extraInfoForGetTokenCall.length() > 0 )
          {
            params.put("extra_info", this.extraInfoForGetTokenCall);
          }
	  
	  if (this.emailAddress != null)
	  {
		  params.put("user_email", this.emailAddress);		  
	  }

	  if (this.password != null)
	  {
		  params.put("user_pwd", this.password);		  
	  }

	  if (this.userKey != null)
	  {
		  params.put("user_key", this.userKey);
	  }
	  
      String request = this.prepareRequest(params, "getToken");
      
      String response;
      try {   	  
    	  response = this.getResponse(request);          
      } catch(Exception e) { 
          throw new RuntimeException(e); 
      }                
      
      Object obj=JSONValue.parse(response);
      JSONObject jobj=(JSONObject)obj;

      String token = (String) jobj.get("token");
      
      if (token == null)
      {
    	  throw new PlancakeApiException("problem resetting the token - " + response);
      }
      
      this.token = token;
  }

  private JSONObject sendRequest(Map<String, String> params, String methodName) 
  		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException
  {
	  return this.sendRequest(params, methodName, "GET");
  }

  /**
  *
  * @param Map<String, String> params
  * @param string methodName
  * @param array $requestOpts (= null) - the options for the HTTP request
  * @param String httpMethod ('GET' or 'POST')
  * @return stdClass - result of json_decode
  */
 private JSONObject sendRequest(Map<String, String> params, String methodName, String httpMethod) 
 		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException
 { 
     if (this.token == "")
     {
         this.resetToken();
     }
    
     String request = this.prepareRequest(params, methodName);

     String response = this.getResponse(request, httpMethod);

     // Debug
     //System.out.println("request:  " + request);
     //System.out.println("response:  " + response);
     
     Object obj=JSONValue.parse(response);
     JSONObject jsonResponse =(JSONObject)obj;
     
     // checking whether an error occurred
     Object error = jsonResponse.get("error");          
     if (error != null)
     {
    	 // if the error is an INVALID_TOKEN_ERROR, we try to get the token again
    	 // (maybe it was just expired)
         if (error.toString().equals(Integer.toString(PlancakeApiClient.INVALID_TOKEN_ERROR)))
         {
             this.resetToken();

             request = this.prepareRequest(params, methodName);
             response = this.getResponse(request, httpMethod);

             obj=JSONValue.parse(response);
             jsonResponse =(JSONObject)obj;      
             error = jsonResponse.get("error");                
             
             if (error != null)
             {
                throw new PlancakeApiException("Error " + error);
             }
         }
         else
         {
             throw new PlancakeApiException("Error " + error);
         }
     }

     return jsonResponse;
 }  
  

	public int getServerTime() 
		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException	
	{
		Map<String, String> params = new HashMap<String, String>();
        String methodName = "getServerTime";

        JSONObject jsonResponse = this.sendRequest(params, methodName);    
        
        return Integer.parseInt(String.valueOf(jsonResponse.get("time")));           
	}
	

    /**
    *
    * @return Map<String, String>
    */
   public PlancakeSettingsForApi getUserSettings()
		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException   
   {
		Map<String, String> params = new HashMap<String, String>();
        String methodName = "getUserSettings";

        JSONObject jsonResponse = this.sendRequest(params, methodName);
        
        PlancakeSettingsForApi settings = new PlancakeSettingsForApi();

        JSONObject timezone = (JSONObject)jsonResponse.get("timezone");
        settings.timezoneDescription = timezone.get("description").toString();
        settings.timezoneOffset = timezone.get("offset").toString();
        settings.timezoneDst = Utils.convertFromStringToBoolean(timezone.get("dst").toString());
        settings.dateFormat = jsonResponse.get("date_format").toString();
        settings.timeFormat = Utils.convertFromStringToBoolean(jsonResponse.get("time_format").toString());
        settings.dstActive = Utils.convertFromStringToBoolean(jsonResponse.get("dst_active").toString());        
        settings.weekStart = Utils.convertFromStringToBoolean(jsonResponse.get("week_start").toString());
        settings.lang = jsonResponse.get("lang").toString();
        settings.clientLangString = jsonResponse.get("client_lang_string").toString();
		
        return settings;
   }

   /**
    * @param int timestamp (=null) - to return only the lists created or edited after this timestamp (GMT)
    * @return PlancakeListForApi[]
    */   
   public List<PlancakeListForApi> getLists()
   		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException    
   {
	   return this.getLists(0, 0);
   }
   
   /**
    * @param long fromTimestamp - to return only the lists created or edited after this timestamp (GMT)
    * @param long toTimestamp - to return only the lists created or edited till this timestamp (GMT)
    * @return PlancakeListForApi[]
    */
   public List<PlancakeListForApi> getLists(long fromTimestamp, long toTimestamp)
		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException    
   {
	   Map<String, String> params = new HashMap<String, String>();
       String methodName = "getLists";

       if (fromTimestamp > 0)
       {
    	   params.put("from_ts", Long.toString(fromTimestamp));
    	   params.put("to_ts", Long.toString(toTimestamp));
       }

       JSONObject jsonResponse = this.sendRequest(params, methodName);           
              
       JSONArray jsonArrayLists = (JSONArray)jsonResponse.get("lists");       

       JSONObject jsonList = null;
       
       PlancakeListForApi list;
       List<PlancakeListForApi> lists = new ArrayList<PlancakeListForApi>();       
       
       for(int i = 0 ; i < jsonArrayLists.size(); i++){
    	   jsonList = (JSONObject)jsonArrayLists.get(i);
    	   list = new PlancakeListForApi();
    	   list.id = Integer.parseInt(jsonList.get("id").toString());
    	   list.name = jsonList.get("name").toString();
    	   list.sortOrder = Integer.parseInt(jsonList.get("sort_order").toString());
    	   list.isInbox = Utils.convertFromStringToBoolean(jsonList.get("is_inbox").toString());
    	   list.isHeader = Utils.convertFromStringToBoolean(jsonList.get("is_header").toString());
    	   list.createdAt = Long.parseLong(jsonList.get("created_at").toString());
    	   list.updatedAt = Long.parseLong(jsonList.get("updated_at").toString());
    	   lists.add(list);
       }
       
       return lists;       
   }

   /**
    * @param long fromTimestamp - to return only the lists deleted after this timestamp (GMT)
    * @param long toTimestamp - to return only the lists deleted till this timestamp (GMT)
    * @return List<Integer> - set of list ids
    */
   public ArrayList<Integer> getDeletedLists(long fromTimestamp, long toTimestamp)
   		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException  
   {
	   Map<String, String> params = new HashMap<String, String>();
       String methodName = "getDeletedLists";
	   
    	   params.put("from_ts", Long.toString(fromTimestamp));
    	   params.put("to_ts", Long.toString(toTimestamp));

       JSONObject jsonResponse = this.sendRequest(params, methodName);           
       
       JSONArray jsonArrayListIds = (JSONArray)jsonResponse.get("list_ids");  
       
       JSONObject jsonListId = null;
       
       PlancakeListForApi list;
       
       ArrayList<Integer> listIds = new ArrayList<Integer>();       
       
       String[] commaSeparatedListIds = Utils.fromJsonArrayToStringArray(jsonArrayListIds.toString());

       int tempItemId = 0;
       
       for(int i = 0 ; i < commaSeparatedListIds.length; i++)
       {
           try
           {
                tempItemId = new Integer(commaSeparatedListIds[i]);
                listIds.add(tempItemId);
           }
           catch(NumberFormatException e)
           {
           }
       }

       return listIds;
   }   

   /**
    * @param int timestamp (=null) - to return only the tags created or edited after this timestamp (GMT)
    * @return PlancakeTagForApi[]
    */   
   public List<PlancakeTagForApi> getTags()
   		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException    
   {
	   return this.getTags(0, 0);
   }
   
   /**
    * @param long fromTimestamp - to return only the lists created or edited after this timestamp (GMT)
    * @param long toTimestamp - to return only the lists created or edited till this timestamp (GMT)
    * @return PlancakeTagForApi[]
    */
   public List<PlancakeTagForApi> getTags(long fromTimestamp, long toTimestamp)
		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException    
   {
	   Map<String, String> params = new HashMap<String, String>();
       String methodName = "getTags";

       if (fromTimestamp > 0)
       {
    	   params.put("from_ts", Long.toString(fromTimestamp));
    	   params.put("to_ts", Long.toString(toTimestamp));
       }

       JSONObject jsonResponse = this.sendRequest(params, methodName);           
              
       JSONArray jsonArrayTags = (JSONArray)jsonResponse.get("tags");       

       JSONObject jsonTag = null;
       int tagId = 0;
       
       PlancakeTagForApi tag;
       List<PlancakeTagForApi> tags = new ArrayList<PlancakeTagForApi>();       
       
       for(int i = 0 ; i < jsonArrayTags.size(); i++){
    	   jsonTag = (JSONObject)jsonArrayTags.get(i);
    	   tag = new PlancakeTagForApi();
    	   tag.id = Integer.parseInt(jsonTag.get("id").toString());
    	   tag.name = jsonTag.get("name").toString();
    	   tag.sortOrder = Integer.parseInt(jsonTag.get("sort_order").toString());
    	   tags.add(tag);
       }
       
       return tags;       
   }

   /**
    * @param long fromTimestamp - to return only the lists created or edited after this timestamp (GMT)
    * @param long toTimestamp - to return only the lists created or edited till this timestamp (GMT)
    * @return List<Integer> - set of list ids
    */
   public ArrayList<Integer> getDeletedTags(long fromTimestamp, long toTimestamp)
   		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException  
   {
	   Map<String, String> params = new HashMap<String, String>();
       String methodName = "getDeletedTags";
	   
        params.put("from_ts", Long.toString(fromTimestamp));
        params.put("to_ts", Long.toString(toTimestamp));

       JSONObject jsonResponse = this.sendRequest(params, methodName);           
       
       JSONArray jsonArrayTagIds = (JSONArray)jsonResponse.get("tag_ids");  
       
       JSONObject jsonTagId = null;
       
       PlancakeTagForApi tag;
       
       ArrayList<Integer> tagIds = new ArrayList<Integer>();       
       
       String[] commaSeparatedTagIds = Utils.fromJsonArrayToStringArray(jsonArrayTagIds.toString());

       int tempItemId = 0;
       
       for(int i = 0 ; i < commaSeparatedTagIds.length; i++)
       {
           try
           {
                tempItemId = new Integer(commaSeparatedTagIds[i]);
                tagIds.add(tempItemId);
           }
           catch(NumberFormatException e)
           {
           }
       }

       return tagIds;
   }

   /**
    * @return PlancakeListForApi[]
    */
   public List<PlancakeRepetitionOptionForApi> getRepetitionOptions()
		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException    
   {
	   return this.getRepetitionOptions(0, 0);
   }
   
   /**
    * @param long fromTimestamp - to return only the lists created or edited after this timestamp (GMT)
    * @param long toTimestamp - to return only the lists created or edited till this timestamp (GMT)
    * @return PlancakeListForApi[]
    */
   public List<PlancakeRepetitionOptionForApi> getRepetitionOptions(long fromTimestamp, long toTimestamp)
		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException    
   {
	   Map<String, String> params = new HashMap<String, String>();
       String methodName = "getRepetitionOptions";

       if (fromTimestamp > 0)
       {
    	   params.put("from_ts", Long.toString(fromTimestamp));
    	   params.put("to_ts", Long.toString(toTimestamp));
       }

       JSONObject jsonResponse = this.sendRequest(params, methodName);           
              
       JSONArray jsonArrayRepetitions = (JSONArray)jsonResponse.get("repetitions");       

       JSONObject jsonRepetition = null;
       int listId = 0;
       
       PlancakeRepetitionOptionForApi repetition;
       List<PlancakeRepetitionOptionForApi> repetitions = new ArrayList<PlancakeRepetitionOptionForApi>();
       
       for(int i = 0 ; i < jsonArrayRepetitions.size(); i++){
    	   jsonRepetition = (JSONObject)jsonArrayRepetitions.get(i);
    	   repetition = new PlancakeRepetitionOptionForApi();
    	   repetition.id = Integer.parseInt(jsonRepetition.get("id").toString());
    	   repetition.label = jsonRepetition.get("label").toString();
    	   repetition.special = jsonRepetition.get("special").toString();
    	   repetition.needsParam = Utils.convertFromStringToBoolean(jsonRepetition.get("needs_param").toString());
    	   repetition.isParamCardinal = Utils.convertFromStringToBoolean(jsonRepetition.get("is_param_cardinal").toString());
    	   repetition.minParam = Integer.parseInt(jsonRepetition.get("min_param").toString());
    	   repetition.maxParam = Integer.parseInt(jsonRepetition.get("max_param").toString());
           repetition.icalRruleTemplate = jsonRepetition.get("ical_rrule_template").toString();
    	   repetition.id = Integer.parseInt(jsonRepetition.get("id").toString());    	       		
    	   repetition.sortOrder = Integer.parseInt(jsonRepetition.get("sort_order").toString());
    	   repetition.hasDividerBelow = Utils.convertFromStringToBoolean(jsonRepetition.get("has_divider_below").toString());    	       	   
    	   repetitions.add(repetition);
       }
       
       return repetitions;       
   }
   
      
   /**
   *
   * @param options
   * @return array
   */
  public List<PlancakeTaskForApi> getTasks(PlancakeTasksFilterOptions options)
		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException      
  {
	  Map<String, String> params = new HashMap<String, String>();
      String methodName = "getTasks";	   
	   
      if (options.fromTimestamp > 0L)
      {
    	   params.put("from_ts", Long.toString(options.fromTimestamp));
    	   params.put("to_ts", Long.toString(options.toTimestamp));
      }
      if (options.taskId > 0L)
          params.put("task_id", Long.toString(options.taskId));
      if (options.listId > 0)
          params.put("list_id", Integer.toString(options.listId));
      if (options.tagId > 0)
          params.put("tag_id", Integer.toString(options.tagId));      

      params.put("completed", Integer.toString(Utils.convertFromBooleanToInt(options.completed)));        
      params.put("only_with_due_date", Integer.toString(Utils.convertFromBooleanToInt(options.onlyWithDueDate)));    
      params.put("only_without_due_date", Integer.toString(Utils.convertFromBooleanToInt(options.onlyWithoutDueDate)));    
      params.put("only_due_today_or_tomorrow", Integer.toString(Utils.convertFromBooleanToInt(options.onlyDueTodayOrTomorrow)));    
      params.put("only_starred", Integer.toString(Utils.convertFromBooleanToInt(options.onlyStarred)));          

      JSONObject jsonResponse = this.sendRequest(params, methodName);           
      
      JSONArray jsonArrayTasks = (JSONArray)jsonResponse.get("tasks");       

      JSONObject jsonTask = null;
      
      PlancakeTaskForApi task;
      List<PlancakeTaskForApi> tasks = new ArrayList<PlancakeTaskForApi>();       
      
      for(int i = 0 ; i < jsonArrayTasks.size(); i++){
   	   jsonTask = (JSONObject)jsonArrayTasks.get(i);
   	   task = new PlancakeTaskForApi();
   	   task.id = Long.parseLong(jsonTask.get("id").toString());
   	   task.listId = Integer.parseInt(jsonTask.get("list_id").toString());
   	   task.description = jsonTask.get("description").toString();
   	   task.sortOrder =  Integer.parseInt(jsonTask.get("sort_order").toString());
   	   task.isStarred =  Utils.convertFromStringToBoolean(jsonTask.get("is_starred").toString());
   	   task.isHeader =  Utils.convertFromStringToBoolean(jsonTask.get("is_header").toString());   	   
   	   task.dueDate =  jsonTask.get("due_date").toString();
   	   task.dueTime =  jsonTask.get("due_time").toString();
   	   task.repetitionId =  Integer.parseInt(jsonTask.get("repetition_id").toString());
   	   task.repetitionParam =  Integer.parseInt(jsonTask.get("repetition_param").toString());
   	   task.repetitionIcalRrule = jsonTask.get("repetition_ical_rrule").toString();
   	   task.isCompleted =  Utils.convertFromStringToBoolean(jsonTask.get("is_completed").toString());
   	   task.isFromSystem =  Utils.convertFromStringToBoolean(jsonTask.get("is_from_system").toString());
   	   task.note =  jsonTask.get("note").toString();   	   
   	   task.tagIds =  jsonTask.get("tags").toString();
   	   task.createdAt =  Long.parseLong(jsonTask.get("created_at").toString());
   	   task.updatedAt =  Long.parseLong(jsonTask.get("updated_at").toString());    	   
	
   	   tasks.add(task);
      }
      
      return tasks; 
  }
  
  /**
    * @param long fromTimestamp - to return only the lists created or edited after this timestamp (GMT)
    * @param long toTimestamp - to return only the lists created or edited till this timestamp (GMT)
   * @return List<Long> - set of list ids
   */
  public ArrayList<Long> getDeletedTasks(long fromTimestamp, long toTimestamp)
  		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException  
  {
	  Map<String, String> params = new HashMap<String, String>();
      String methodName = "getDeletedTasks";
	   
    	   params.put("from_ts", Long.toString(fromTimestamp));
    	   params.put("to_ts", Long.toString(toTimestamp));

      JSONObject jsonResponse = this.sendRequest(params, methodName);           
      
      JSONArray jsonArrayTaskIds = (JSONArray)jsonResponse.get("task_ids");  

      ArrayList<Long> taskIds = new ArrayList<Long>();
      
      String[] commaSeparatedTaskIds = Utils.fromJsonArrayToStringArray(jsonArrayTaskIds.toString());

      long tempItemId = 0;
      
      for(int i = 0 ; i < commaSeparatedTaskIds.length; i++)
      {
           try
           {
               tempItemId = Long.parseLong(commaSeparatedTaskIds[i]);
               taskIds.add(tempItemId);
           }
           catch(NumberFormatException e)
           {
           }
      }


      return taskIds;
  }

  /**
   * @param long taskId
   * @param String baselineDueDate - in the format YYYY-mm-dd
   *        this is to make sure a task is not completed twice but different applications
   * @return taskId
   */
  public long completeTask(long taskId, String baselineDueDate)
  		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException
  {
	  Map<String, String> params = new HashMap<String, String>();
      String methodName = "completeTask";

	  params.put("task_id", Long.toString(taskId));

      if (baselineDueDate.length() > 0)
      {
        params.put("baseline_due_date", baselineDueDate);
      }

      JSONObject jsonResponse = this.sendRequest(params, methodName);

      return Long.parseLong(jsonResponse.get("task_id").toString());
  }

  /**
   * @param long taskId
   * @return taskId
   */
  public long completeTask(long taskId)
  		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException
  {
	  return this.completeTask(taskId, "");
  }
  
  
  /**
   * @param long taskId
   * @return taskId
   */
  public long uncompleteTask(long taskId)
  		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException  
  {
	  Map<String, String> params = new HashMap<String, String>();
      String methodName = "uncompleteTask";
	   
	  params.put("task_id", Long.toString(taskId));

      JSONObject jsonResponse = this.sendRequest(params, methodName);             
      
      return Long.parseLong(jsonResponse.get("task_id").toString());
  }   
  
  
  /**
   * @param long taskId
   * @return taskId
   */
  public long deleteTask(long taskId)
  		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException  
  {
	  Map<String, String> params = new HashMap<String, String>();
      String methodName = "deleteTask";
	   
	  params.put("task_id", Long.toString(taskId));

      JSONObject jsonResponse = this.sendRequest(params, methodName);             
      
      return Long.parseLong(jsonResponse.get("task_id").toString());
  }
  
  /**
  *
  * @param long taskId
  * @param String $note
  * @return int - taskId
  */
 public long setTaskNote(long taskId, String note)
	throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException  
 {
	 Map<String, String> params = new HashMap<String, String>();
     String methodName = "setTaskNote";	   

	 params.put("task_id", Long.toString(taskId));
	 params.put("note", note);   	 

     JSONObject jsonResponse = this.sendRequest(params, methodName, "POST");             
     
     return Long.parseLong(jsonResponse.get("task_id").toString());
 }
 
	/**
	 *
	 * @param PlancakeTaskForApi task
	 * @return long - taskId
	 */
	public long addTask(PlancakeTaskForApi task)
		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException  
	{
		Map<String, String> params = new HashMap<String, String>();
	    String methodName = "addTask";	   

		params.put("descr", task.description);  	    
		params.put("is_header",  Integer.toString(Utils.convertFromBooleanToInt(task.isHeader)));  	
		params.put("is_starred",  Integer.toString(Utils.convertFromBooleanToInt(task.isStarred))); 

		if (task.listId > 0)
			params.put("list_id", Integer.toString(task.listId));

		if (task.dueDate != null)
			params.put("due_date", task.dueDate);				

		if (task.dueTime != null)
			params.put("due_time", task.dueTime);
		
		if (task.repetitionId > 0)
			params.put("repetition_id", Integer.toString(task.repetitionId));

		if (task.repetitionParam > 0)
			params.put("repetition_param", Integer.toString(task.repetitionParam));

		if (task.repetitionIcalRrule != null)
			params.put("repetition_ical_rrule", task.repetitionIcalRrule);
		
		if (task.note != null)
			params.put("note", task.note);				

		if (task.tagIds != null)
			params.put("tag_ids", task.tagIds);		
	
	    JSONObject jsonResponse = this.sendRequest(params, methodName);             
	    
	    return Long.parseLong(jsonResponse.get("task_id").toString());
	}
	
	
	/**
	 * @param long taskId - the task to edit
	 * @param PlancakeTaskForApi task
	 * @return int - taskId
	 */
   public long editTask(long taskId, PlancakeTaskForApi task)
	throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException   
   {
	   Map<String, String> params = new HashMap<String, String>();
       String methodName = "editTask";	   
	   
       params.put("task_id", Long.toString(taskId));
	   params.put("is_header",  Integer.toString(Utils.convertFromBooleanToInt(task.isHeader)));  	
	   params.put("is_starred",  Integer.toString(Utils.convertFromBooleanToInt(task.isStarred)));        
       
       if (task.description != null)
    	   params.put("descr", task.description);
		
       if (task.listId > 0)
    	   params.put("list_id", Integer.toString(task.listId));
		
       if (task.dueDate != null)
    	   params.put("due_date", task.dueDate);				

       if (task.dueTime != null)
           params.put("due_time", task.dueTime);
		
       if (task.repetitionId > 0)
    	   params.put("repetition_id", Integer.toString(task.repetitionId));
		
       if (task.repetitionParam > 0)
    	   params.put("repetition_param", Integer.toString(task.repetitionParam));		

       if (task.repetitionIcalRrule != null)
               params.put("repetition_ical_rrule", task.repetitionIcalRrule);
		
       if (task.note != null)
    	   params.put("note", task.note);				
		
       if (task.tagIds != null)
    	   params.put("tag_ids", task.tagIds);	

       JSONObject jsonResponse = this.sendRequest(params, methodName);             
		
       return Long.parseLong(jsonResponse.get("task_id").toString());
   }	

  /**
    * @param long fromTimestamp - to return only the lists created or edited after this timestamp (GMT)
    * @param long toTimestamp - to return only the lists created or edited till this timestamp (GMT)
   * @return List<Long> - set of list ids
   */
  public String[] whatHasChanged(long fromTimestamp, long toTimestamp)
  		throws PlancakeApiException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, MalformedURLException, IOException
  {
	  Map<String, String> params = new HashMap<String, String>();
      String methodName = "whatHasChanged";

    	   params.put("from_ts", Long.toString(fromTimestamp));
    	   params.put("to_ts", Long.toString(toTimestamp));

      JSONObject jsonResponse = this.sendRequest(params, methodName);

      JSONArray jsonArrayChanged = (JSONArray)jsonResponse.get("changed");

      // the fromJsonArrayToStringArray method has a bug
      String[] temp = Utils.fromJsonArrayToStringArray(jsonArrayChanged.toString());

      for(int j=0; j < temp.length; j++)
      {
     	temp[j] = temp[j].replace("\"", "");
      }

      return temp;
  }

}
