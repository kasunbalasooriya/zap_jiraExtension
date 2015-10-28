/**
 * Created by kasun on 10/21/15.
 */


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import javax.naming.AuthenticationException;
public class JiraRestClient {

    private static String BASE_URL = "http://localhost:8081";

    public static void main(String[] args) {

		String auth = new String(Base64.encode("kmbkck1@gmail.com:kmbkck211"));

		try {
			//Get Projects
			String projects = invokeGetMethod(auth, BASE_URL+"/rest/api/2/project");
            //System.out.println(projects);
            JSONArray projectArray = new JSONArray(projects);
			for (int i = 0; i < projectArray.length(); i++) {
				JSONObject proj = projectArray.getJSONObject(i);
				System.out.println("Key:"+proj.getString("key")+", Name:"+proj.getString("name"));
			}

			//Create Issue

//			String createIssueData = "{\"fields\": {\"project\": {\"key\":\"PROD\"}," +
//					"\"summary\":\"REST Test\",\"description\": \"Creating of an issue using project keys and issue type " +
//					"names using the REST API\",\"issuetype\":{\"name\":\"Bug\"},\"priority\":{\"name\":\"High\"}}}";

            HtmlParser htmlParser=new HtmlParser();
            Document doc =htmlParser.ReadHtmldoc("res", "sample.html");
            String issueList[] = htmlParser.CreateIssueList(doc, "PROD");

            int issueCount = Integer.parseInt(issueList[999]);
            for (int i = 0; i < issueCount; i++) { //create Issues in jira
                System.out.println("Issuelist " +i+issueList[i]);
                String issue = invokePostMethod(auth, BASE_URL + "/rest/api/2/issue", issueList[i]);
                System.out.println(issue);
                //JSONObject issueObj = new JSONObject(issue);
                // String newKey = issueObj.getString("key");
                //System.out.println("Key:" + newKey);
            }


//			//Update Issue
//			String editIssueData = "{\"fields\":{\"assignee\":{\"name\":\"test\"}}}";
//			invokePutMethod(auth, BASE_URL+"/rest/api/2/issue/"+newKey, editIssueData);
//
//			invokeDeleteMethod(auth, BASE_URL+"/rest/api/2/issue/DEMO-13");

		} catch (AuthenticationException e) {
			System.out.println("Username or Password wrong!");
			e.printStackTrace();
		} catch (ClientHandlerException e) {
			System.out.println("Error invoking REST method");
			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("Invalid JSON output");
			e.printStackTrace();
		}





    }

    private static String invokeGetMethod(String auth, String url) throws AuthenticationException, ClientHandlerException {
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
                .accept("application/json").get(ClientResponse.class);
        int statusCode = response.getStatus();
        if (statusCode == 401) {
            throw new AuthenticationException("Invalid Username or Password");
        }
        return response.getEntity(String.class);
    }

    private static String invokePostMethod(String auth, String url, String data) throws AuthenticationException, ClientHandlerException {
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
                .accept("application/json").post(ClientResponse.class, data);
        int statusCode = response.getStatus();
        if (statusCode == 401) {
            throw new AuthenticationException("Invalid Username or Password");
        }
        return response.getEntity(String.class);
    }

    private static void invokePutMethod(String auth, String url, String data) throws AuthenticationException, ClientHandlerException {
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
                .accept("application/json").put(ClientResponse.class, data);
        int statusCode = response.getStatus();
        if (statusCode == 401) {
            throw new AuthenticationException("Invalid Username or Password");
        }
    }

    private static void invokeDeleteMethod(String auth, String url) throws AuthenticationException, ClientHandlerException {
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
                .accept("application/json").delete(ClientResponse.class);
        int statusCode = response.getStatus();
        if (statusCode == 401) {
            throw new AuthenticationException("Invalid Username or Password");
        }
    }

}
