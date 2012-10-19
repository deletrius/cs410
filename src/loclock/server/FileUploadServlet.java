package loclock.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

public class FileUploadServlet extends HttpServlet {

//	private static final String UPLOAD_DIRECTORY = "d:\\uploaded\\";
//	 
//	     @Override
//	     protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//	             throws ServletException, IOException {
//	    	 
//	    	 System.out.println("IN GET");
//	    	 
//	         super.doGet(req, resp);
//	     }
//	 
//	     @Override
//	     protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//	             throws ServletException, IOException {
//	    	 System.out.println("IN POST");
//	    	 System.out.println(req.toString());
//	         // process only multipart requests
//	         if (ServletFileUpload.isMultipartContent(req)) {
//	 
//	             // Create a factory for disk-based file items
//	             FileItemFactory factory = new DiskFileItemFactory();
//	 
//	             // Create a new file upload handler
//	             ServletFileUpload upload = new ServletFileUpload(factory);
//	 
//	             // Parse the request
//	             try {
//	                 List<FileItem> items = upload.parseRequest(req);
//	                 for (FileItem item : items) {
//	                     // process only file upload - discard other form item types
//	                     if (item.isFormField()) continue;
//	                     
//	                     String fileName = item.getName();
//	                     // get only the file name not whole path
//	                     if (fileName != null) {
//	                         fileName = FilenameUtils. getName(fileName);
//	                     }
//	 
//	                     File uploadedFile = new File(UPLOAD_DIRECTORY, fileName);
//	                     if (uploadedFile.createNewFile()) {
//	                         item.write(uploadedFile);
//	                         resp.setStatus(HttpServletResponse.SC_CREATED);
//	                         resp.getWriter().print("The file was created successfully.");
//	                         resp.flushBuffer();
//	                     } else
//	                         throw new IOException("The file already exists in repository.");
//	                 }
//	             } catch (Exception e) {
//	                 resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
//	                         "An error occurred while creating the file : " + e.getMessage());
//	             }
//	 
//	         } else {
//	             resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
//	                             "Request contents type is not supported by the servlet.");
//	         }
//	     }
	
	
	private static final Logger log = Logger.getLogger(FileUpload.class
			.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {
			ServletFileUpload upload = new ServletFileUpload();
			res.setContentType("text/plain");

			FileItemIterator iterator = upload.getItemIterator(req);
			while (iterator.hasNext()) {
				FileItemStream item = iterator.next();
				InputStream stream = item.openStream();

				if (item.isFormField()) {
					log.warning("Got a form field: " + item.getFieldName());
				} else {
					log.warning("Got an uploaded file: " + item.getFieldName()
							+ ", name = " + item.getName());

					// You now have the filename (item.getName() and the
					// contents (which you can read from stream). Here we just
					// print them back out to the servlet output stream, but you
					// will probably want to do something more interesting (for
					// example, wrap them in a Blob and commit them to the
					// datastore).
					int len;
					byte[] buffer = new byte[8192];
					while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
						res.getOutputStream().write(buffer, 0, len);
					}
				}
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
}
