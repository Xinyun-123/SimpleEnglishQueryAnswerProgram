class item {
	String name;
	Double[] tfidf;

	item(String name) {
	   this.name = name;
	   this.tfidf = new Double[225];
	   for(int i=0;i<tfidf.length;i++) {
		   tfidf[i]= (double) 0;
	   }
	}
	item(String name, Double[] tfidf){
		this.name=name;
		this.tfidf=tfidf;
	}
}

class item1{
	String name;
	Double[] tfidf;
	
	item1(String name){
		this.name = name;
		   this.tfidf = new Double[1400];
		   for(int i=0;i<tfidf.length;i++) {
			   tfidf[i]=(double) 0;
		   }
	}
	item1(String name, Double[] tfidf){
		this.name=name;
		this.tfidf=tfidf;
	}
}

class numbers{
	Double a;
	Double[] b;
	numbers(Double a, Double[] b){
		this.a=a;
		this.b=b;
	}
}

public class SimpleQAProgram {
	public static void main(String args[]) throws IOException {
		String file = args[0];
		String file1 = args[1];
		BufferedReader queries = new BufferedReader(new FileReader(args[0]));
		BufferedReader abstracts = new BufferedReader(new FileReader(args[1]));
		

		String[] closed_class_stop_words = {"a","the","an","and","or","but","about","above","after","along","amid","among",
                           "as","at","by","for","from","in","into","like","minus","near","of","off","on",
                           "onto","out","over","past","per","plus","since","till","to","under","until","up",
                           "via","vs","with","that","can","cannot","could","may","might","must",
                           "need","ought","shall","should","will","would","have","had","has","having","be",
                           "is","am","are","was","were","being","been","get","gets","got","gotten",
                           "getting","seem","seeming","seems","seemed",
                           "enough", "both", "all", "your", "those", "this", "these", 
                           "their", "the", "that", "some", "our", "no", "neither", "my",
                           "its", "his", "her", "every", "either", "each", "any", "another",
                           "an", "a", "just", "mere", "such", "merely", "right", "no", "not",
                           "only", "sheer", "even", "especially", "namely", "as", "more",
                           "most", "less", "least", "so", "enough", "too", "pretty", "quite",
                           "rather", "somewhat", "sufficiently", "same", "different", "such",
                           "when", "why", "where", "how", "what", "who", "whom", "which",
                           "whether", "why", "whose", "if", "anybody", "anyone", "anyplace", 
                           "anything", "anytime", "anywhere", "everybody", "everyday",
                           "everyone", "everyplace", "everything", "everywhere", "whatever",
                           "whenever", "whereever", "whichever", "whoever", "whomever", "he",
                           "him", "his", "her", "she", "it", "they", "them", "its", "their",
                           "theirs","you","your","yours","me","my","mine","I","we","us","much","and/or"};

		
		BufferedWriter out = new BufferedWriter(new FileWriter("output.txt"));
		String line;
		
		
		//Calculate the occurrence of words of the queries
		ArrayList<item> ques = new ArrayList<item>();
		int count_q=0;	//query number
		while((line = queries.readLine())!=null){
			String[] words = line.split("\\s+");	
 			if((!words[0].equals(".I"))&&(!words[0].equals(".W"))) {					
				words = line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");	//remove punctuation and numbers			
				for(int i=0;i<words.length;i++) {
					if(!isFound(words[i],closed_class_stop_words)) {			//removing stop words
						if(isFoundinArrayList(words[i], ques)==-1) {			//check if the word is in the arraylist
							item a = new item(words[i]);
							a.tfidf[count_q-1]=(double) 1;
							ques.add(a);
						}else {
							int index = isFoundinArrayList(words[i],ques);
							Double[] newA = ques.get(index).tfidf;
							newA[count_q-1]+=1;
							item newI = new item(words[i],newA);
							ques.set(index, newI);						
						}
					}
				}
								
			}
 			if(words[0].equals(".I")) {
 				count_q++;
 			}		
		}



		//Calculate TFIDF of queries
		for(int i=0; i<ques.size();i++) {
			int cal=0;
			for(int j=0;j<ques.get(i).tfidf.length;j++) {
				if(ques.get(i).tfidf[j]>0)
					cal++;
			}
			Double idf = Math.log(225/cal);
			Double[] temp = ques.get(i).tfidf;
			for(int j=0;j<temp.length;j++) {
				if(temp[j]!=0)
					temp[j]=temp[j]*idf;
			}
			item newI = new item(ques.get(i).name,temp);
			ques.set(i, newI);
		}
		


		
		
		//Calculate occurrence of words of abstracts
		String line1;
		int count_a=1;
		ArrayList<item1> abs = new ArrayList<item1>();
		line1 = abstracts.readLine();
		while((line1 = abstracts.readLine())!=null){
			String[] check = line1.split("\\s+");	
 			if(check[0].equals(".W")) {
				while(true){
					line1 = abstracts.readLine();
					if(line1==null)
						break;
					check = line1.split("\\s+");
					if(check[0]!=null && check[0].equals(".I")){
						count_a++;
						break;
					}
					String[] words = line1.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");	//remove punctuation and numbers
					if(words.length==0)
						continue;
					for(int i=0;i<words.length;i++) {
						if(!isFound(words[i],closed_class_stop_words)) {			//removing stop words
							if(isFoundinArrayList(words[i],ques)!=-1) {
								if(isFoundinArrayList1(words[i], abs)==-1) {			//check if the word is in the arraylist
									item1 a = new item1(words[i]);
									a.tfidf[count_a-1]=(double) 1;
									abs.add(a);
								}else {
									int index = isFoundinArrayList1(words[i],abs);
									Double[] newA = abs.get(index).tfidf;
									newA[count_a-1]+=1;
									item1 newI = new item1(words[i],newA);
									abs.set(index, newI);						
								}
								
							}						
							
						}

					}
				
				}
				if(line1==null)
					break;
								
			}
		}



		
		//Calculate TFIDF of abstracts
		for(int i=0; i<abs.size();i++) {
			int cal=0;
			for(int j=0;j<abs.get(i).tfidf.length;j++) {
				if(abs.get(i).tfidf[j]>0)
					cal+=1;
				}
			if(cal==0)
				continue;
			Double idf = Math.log(1400/cal);
			Double[] temp = abs.get(i).tfidf;
			for(int j=0;j<temp.length;j++) {
				if(temp[j]!=0)
					temp[j]=temp[j]*idf;
			}
			item1 newI = new item1(abs.get(i).name,temp);
			abs.set(i, newI);
		}




		
		//Calculate Cosine Similarity
		
		ArrayList<numbers> match = new ArrayList<numbers>();

		for(int i=0;i<225;i++) {	
				for(int j=0;j<ques.size();j++) {
					if(ques.get(j).tfidf[i]!=0) {
						Double a = ques.get(j).tfidf[i];
						int index = isFoundinArrayList1(ques.get(j).name,abs);
						if(index!=-1) {
							Double[] b = abs.get(index).tfidf;
							numbers num = new numbers(a,b);
							match.add(num);
								
						}
					}
					
				}
				HashMap<Integer,Double> cos_s = similarityCalc(match);
				for(int a_id: cos_s.keySet()) {
					out.write(i+1 + " " + a_id + " " + cos_s.get(a_id) + "\n");
				}
				
		}
		out.close();
		queries.close();
		abstracts.close();
		
	}
	
	public static boolean isFound(String word, String[] words) {
		for(int i=0;i<words.length;i++) {
			if(words[i].equals(word))
				return true;
		}		
		return false;		
	}
	
	public static int isFoundinArrayList(String word, ArrayList<item> items) {
		for(int i=0;i<items.size();i++) {
			if(items.get(i).name.equals(word))
				return i;
		}
		return -1;
	}
	
	public static int isFoundinArrayList1(String word, ArrayList<item1> items1) {
		for(int i=0;i<items1.size();i++) {
			if(items1.get(i).name.equals(word))
				return i;
		}
		return -1;
	}
	
	public static HashMap<Integer, Double> similarityCalc(ArrayList<numbers> match){
		HashMap<Integer, Double> sim_table = new HashMap<>();
		Double nume = (double) 0; //numerator
		Double deno1 =(double) 0;  //denominator
		Double deno2= (double) 0;
		Double cosS;
		for(int i=0;i<1400;i++) {
			for(int j=0;j<match.size();j++) {
				Double a = match.get(j).a;
				Double b = match.get(j).b[i];
				if(i==485){
					System.out.println(a+" "+b);
				}
				nume+=a*b;
				deno1+=Math.pow(a, 2);
				deno2+=Math.pow(b, 2);
			}
			if(deno1*deno2==0)
				cosS=(double) 0;
			else 
				cosS=nume/Math.sqrt(deno1*deno2);			
			sim_table.put(i+1, cosS);
			if(i==485){
				System.out.println("///////cos= "+ cosS);
			}

		}
		
		return sim_table;
		
	}
}
