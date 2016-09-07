package pack;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
public class FirstServlet extends HttpServlet
{
	private int apiIndex,apiCount;

	 public void init() 
  { 
     // Reset hit counter.
	    apiCount = 0;
  		apiIndex = 0;
  } 

	public static void main(String[] args) {
		System.out.println(Test.run("hello world this is ajay aneja","60ad303c39ce16e8e24ac0d78605ddc70f599bd3"));
		
	}
	public void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException
	{
		int totalApi=15;
		String apiKeys[] = new String[totalApi];
		apiKeys[0]="f6340770a767163ca394c8f5aa47dc5351fdd103";
		apiKeys[1]="703bd4e0f8c97d8e9365a3b31fc276d1e16ec499";
		apiKeys[2]="2fcc21a0605a2170fe1cc138b9f4f36e980a8a4a";
		apiKeys[3]="84f7c7fdaefa97caafa3f0b16afbff3ac4e4998e";
		apiKeys[4]="69b674a8fc0b650f9cc857d9c7abe4e8a1169ca2";
		apiKeys[5]="82d0575a361238f791adaf8b3bacf6782c0e4888";
		apiKeys[6]="6c3516dffa769227b4a57ca4457711e5b2a1cd46";
		apiKeys[7]="b9ee89c49e129f428c33031ca8a278ce4248306f";
		apiKeys[8]="2b68a5bfbda68e3c12c88dd60ab32d14948ce328";
		apiKeys[9]="60ad303c39ce16e8e24ac0d78605ddc70f599bd3";
		apiKeys[10]="4ff693b80bfacab882959b52cf1954c02ccfdab5";
		apiKeys[11]="f866314d9361dc6f827fe1405782576423b932f6";
		apiKeys[12]="d01dbde44977709a57ebc79e56543ebc4bd6ad93";
		apiKeys[13]="7f7e353d293955d4a3e70d0773c4345fb549129f";
		apiKeys[14]="f35f423fcbc7769d62e0a81737098f8fe7b0e0ad";
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		try{
		String productLink = req.getParameter("usr");
		String reviewLink = saveproductpage.extract(productLink);
		//ReviewDownloader re ;
		int count =0;
		//out.println("ajay <br>");
		
		ArrayList<String> reviews = ReviewDowloader.reviewDownload(reviewLink);
		if(reviews.size()==0)
		{
			RequestDispatcher requestDispatcher; 
			requestDispatcher = req.getRequestDispatcher("/zero.jsp");
			requestDispatcher.forward(req, res);
		}
		else{

			ArrayList<Pair> positive = new ArrayList<Pair>();
			ArrayList<Pair> negative = new ArrayList<Pair>();
			ArrayList<Pair> neutral = new ArrayList<Pair>();
			//out.println(reviews.size());
			for(String re : reviews)
			{
				// out.println(re);

				// out.println(++count);
				// out.println(re);
				// //out.println(Test.run(re));
				apiCount++;
				if(apiCount==1000*totalApi)
				{
					apiCount=0;
					apiIndex=-1;
				}
				if(apiCount%1000==0)
				{
					apiIndex = (apiIndex+1)%totalApi;
				}

				float res1 = Test.run(re,apiKeys[apiIndex]);
				Pair p = new Pair(res1,re);
//				out.println(" "+res1+" <br>");
				if(res1>0)
				{		
					positive.add(p);
				}
				else if(res1<0)
				{
					negative.add(p);
				}
				else
				{
					neutral.add(p);
				}
				
				/*ArrayList<String> sentences  = Summarizer.splitToSentences(re);
				for(String s : sentences)
				{
						out.println(s);
					try{
						out.println(Client1.run(s));
					}
						catch(Exception e)
					{
						out.println(e);

					}
						out.println("<br>");
				}*/	
			//	out.println("<br>");
			} 
			//out.println("ajay"+apiCount);;
			Collections.sort(positive,new Comparator<Pair>(){
				@Override public int compare(Pair p1 ,Pair p2)
				{
					float fs = p1.getScore()-p2.getScore();
					if(fs>0)
					return -1;
					else if(fs<0)
					return  1;
					return 0;
				}
			});
			Collections.sort(negative,new Comparator<Pair>(){
				@Override public int compare(Pair p1 ,Pair p2)
				{
					float fs = p1.getScore()-p2.getScore();
					if(fs>0)
					return 1;
					else if(fs<0)
					return  -1;
					return 0;
				}
			});
			int i;
			ArrayList<String> positiv = new ArrayList<String>();
			for(i=0;i<10&&i<positive.size();i++)
			{
				Pair p1 = positive.get(i);
				positiv.add(p1.getReview());
			// 	out.println(p1.getScore());
			// 	out.println(p1.getReview());
			// 	out.println("<br>");
			// 
			}
			ArrayList<String> negativ = new ArrayList<String>();
			for(i=0;i<10&&i<negative.size();i++)
			{
				Pair p1 = negative.get(i);
				negativ.add(p1.getReview());
				out.println(p1.getScore());
				out.println("<br>");
			}
				String color="";
				float total = positive.size() + negative.size() + neutral.size();
				float a = (positive.size()/total)*100;
				float b = (negative.size()/total)*100;
				float c  = (neutral.size()/total)*100;
				if(a>b)
				{
					if(a>c)
						color = "#00bfff";
					else
						color = "32cd32";
				}	
				else
				{
					if(b>c)
						color = "d3d3d3";
					else
						color="32cd32";
				}

				req.setAttribute("Positive",(Float)a);
				req.setAttribute("Negative",(Float)b);
				req.setAttribute("Neutral",(Float)c);
				req.setAttribute("posReview",positiv);
				req.setAttribute("negReview",negativ);
				req.setAttribute("color",(String)color);
				 out.println(positive.size()+"   "+a+"<br>");
				 out.println(negative.size()+" "+b+"<br>");
				 out.println(neutral.size()+" "+c+"<br>");
				 RequestDispatcher requestDispatcher; 
				requestDispatcher = req.getRequestDispatcher("/result.jsp");
				requestDispatcher.forward(req, res);
				
				//out.println(reviewLink);
				//res.sendRedirect("result.jsp");
	}
	}
	catch(Exception e)
	{
		RequestDispatcher requestDispatcher; 
				req.setAttribute("exc",e);
				requestDispatcher = req.getRequestDispatcher("/exception.jsp");
				requestDispatcher.forward(req, res);
	}

	}
	public void doGet(HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException
	{
		res.sendRedirect("index.html");
	}
}