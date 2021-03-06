package com.niit.discussionB.restControllers;


import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.niit.discussionB.dao.FileUploadDAO;
import com.niit.discussionB.model.FileUpload;



@RestController
public class FileUploadController
{
	private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);
	
	@Autowired
	private FileUploadDAO fileUploadDAO;
	
	@Autowired
	private HttpSession session;

	@RequestMapping(value="/Upload",method=RequestMethod.POST)
	public ModelAndView uploadFile(@RequestParam("uploadedFile") MultipartFile uploadFile)  throws Exception
	{
		if (uploadFile != null ) 
		{
			if(session.getAttribute("username")!=null)
			{
			System.out.println(session.getAttribute("username").toString());
			String username = session.getAttribute("username").toString();
			MultipartFile aFile = uploadFile;
			FileUpload fileUpload = new FileUpload();
			System.out.println(aFile);
			fileUpload.setFileName(session.getAttribute("username").toString());
            fileUpload.setData(aFile.getBytes());//image 
            fileUpload.setUsername(username);//login details
            fileUploadDAO.save(fileUpload, username);
            
            FileUpload getFileUpload = fileUploadDAO.getFile(username);
         	String name = getFileUpload.getFileName();
         	System.out.println("Name = "+name);
         	System.out.println(getFileUpload.getData());
         	byte[] imagefiles = getFileUpload.getData();  //image
         	try
         	{
         		//change the path according to your workspace and the name of your project
         		String path="F:/CollaborationProject2/discussionF/WebContent/resources/images/"+username+".jpg";
         		System.out.print(path);
         		File file=new File(path);
         		//file.mkdirs();
         		FileOutputStream fos = new FileOutputStream(file);
         		fos.write(imagefiles); // write the array of bytes in username file.
         		fos.close();
         		}catch(Exception e){
         		e.printStackTrace();
         	}
		}
		}
		else
		{
			System.out.println("Session equal to null");
		}
		ModelAndView mv = new ModelAndView("backToHome");
		return mv;
	}
}