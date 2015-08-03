package controllers;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import play.*;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import views.html.*;

public class Application extends Controller {
  
	static String path = "image/";
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
	public static Result uploadPage() {
	    return ok(image.render());
	}
	
	public static Result save() {
		MultipartFormData body = request().body().asMultipartFormData();
  		 FilePart my_image = body.getFile("Image");	
		String image_name = "image_image.jpg";
	    
	    File file_type = my_image.getFile();
	    				    
	    try {
            FileUtils.copyFile(file_type, new File("image/photos", image_name));
	    } catch (IOException ioe) {
            System.out.println("Problem operating on filesystem");
        }
		return ok(show.render());
	}

    public static Result at(String file){
       File myfile = new File (path + file);
       System.out.println(path + file);
       return ok(myfile);
    }
  
}
