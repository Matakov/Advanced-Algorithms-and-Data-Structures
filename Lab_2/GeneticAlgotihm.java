import java.awt.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class GeneticAlgotihm {
	
	int numberOfCities = 0;
	ArrayList<Point> cities = null;
	int elitism = 1;
	static float mutationProbability = (float) 0.5;
	float mutationScale = (float) 0.1;
	float errorTreshold = 10^(-6);
	int numIterations = 10000;
	int populationSize = 10;
	Map<Integer, Point> dictionary = null;
	HashMap<ArrayList<Integer>, Double> dictionaryOfAllDistances = null;
	double maxDistance = 0;
	
	
	//Konstruktor
	public GeneticAlgotihm(
							ArrayList<Point> input_cities,
							int input_elitism, 
							float input_mutationProbability, 
							int input_numIterations , 
							int input_population
							){
		numberOfCities = input_cities.size();
		cities = input_cities;
		elitism = input_elitism;
		mutationProbability = input_mutationProbability;
		numIterations = input_numIterations;
		populationSize = input_population;
		dictionary = new HashMap<Integer, Point>();
		dictionaryOfAllDistances = new HashMap<ArrayList<Integer>, Double>();
		
		for (int j = 0;j< numberOfCities;j++){
			dictionary.put(j, input_cities.get(j));
		}
		
		
		int j=0;
		for (int i = 0;i< numberOfCities;i++){
			ArrayList<Integer> keyCities = new ArrayList<Integer>();
			if(i+1>=numberOfCities){
				j=0;
			}
			else{
				j=i+1;
			}
			keyCities.add(i);
			keyCities.add(j);
			
			keyCities.removeAll(Collections.singleton(null));
			//System.out.println(keyCities);
			//System.out.println(euclidianDistance(dictionary.get(i),dictionary.get(j)));
			dictionaryOfAllDistances.put(keyCities, euclidianDistance(dictionary.get(i),dictionary.get(j)));
		}
		maxDistance = Collections.max(dictionaryOfAllDistances.values());
		//**/
		//double criteria = getCriteria();
		
	}
	
	//Pomoæna funkcija za ispis rjeènika
	public void printCities(){
		System.out.println(dictionary);
	}
	
	
	//Pomoæna funkcija za generiranje kromosoma
	public ArrayList<Integer> chromosomePopulation(){
		
		ArrayList<Integer> chromosome = new ArrayList<Integer>();
		int numCity = 0;
		
		while (chromosome.size()<numberOfCities){
			numCity=(int) (Math.random() * (numberOfCities));
			if (!chromosome.contains(numCity)){
				chromosome.add(numCity);
			}
		}
		
		return chromosome;
		
	}
	//**
	
	public ArrayList<Integer> greedyCities(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		int i = list.size();
		int j = 0;
		while(list.size()<numberOfCities){
			i = list.size();
			j = i+1;
			if(i==0){
				list.add(i);
			}
			HashMap<Integer, Double> dictionaryCurrent = new HashMap<Integer, Double>();
			while(i==list.size()){
				for(int k=0;k<numberOfCities;k++){
					if(!list.contains(k) && k!=i){
						dictionaryCurrent.put(k, euclidianDistance(dictionary.get(list.get(i-1)),dictionary.get(k)));	
					}
				}
				Entry<Integer, Double> min = null;
				for (Entry<Integer, Double> entry : dictionaryCurrent.entrySet()) {
				    if (min == null || min.getValue() > entry.getValue()) {
				        min = entry;
				    }
				}
				list.add(min.getKey());
			}
			
		}
		return list;
	}
	
	
	public double getCriteria(){
		ArrayList<Integer> list = greedyCities();
		return calculateFitness(list);
	}
	
	//**/
	
	//Pomoæna funkcija za generiranje prve populacije
	public ArrayList<ArrayList<Integer>> firstPopulation(){
		
		ArrayList<ArrayList<Integer>> population = new ArrayList<ArrayList<Integer>>();
		
		for (int i=0;i<numIterations;i++){
			population.add(chromosomePopulation());
		}
		
		return population;
		
	}
	
	
	//Pomoæna funkcija za ispis populatije
	public void printPopulation(ArrayList<ArrayList<Integer>>population){
		System.out.println(population);
	}
	
	
	//Pomoæna funkcija za raèunanje euklidske udaljenosti
	public double euclidianDistance(Point a,Point b){
		return Math.sqrt(Math.pow(((double)a.x-(double)b.x),2)+Math.pow(((double)a.y-(double)b.y),2));
	}
	
	public double calculateDistance(ArrayList<Integer>chromosome){
			
			int sum = 0;
			int j=1;
			for(int i=0;i<chromosome.size();i++){
				if(i+1>=chromosome.size()){
					j=0;
				}
				else{
					j=i+1;
				}
				
				sum+=euclidianDistance(dictionary.get(chromosome.get(i)),dictionary.get(chromosome.get(j)));
			}
			return sum;
	}
	
	
	
	//Funkcija kojom se raèuna dobrota kromosoma
	public double calculateFitness(ArrayList<Integer>chromosome){
		
		int sum = 0;
		int j=1;
		for(int i=0;i<chromosome.size();i++){
			if(i+1>=chromosome.size()){
				j=0;
			}
			else{
				j=i+1;
			}
			
			sum+=euclidianDistance(dictionary.get(chromosome.get(i)),dictionary.get(chromosome.get(j)));
		}
		
		//Želimo minimazirati funkciju
		//return (Double) 1.0/sum;
		//ILI
		return maxDistance*(numberOfCities+1)-sum;
		
		}
	
	//Pomoæna funkcija za raèunanje dobrote populacije
	public HashMap<ArrayList<Integer>,Double> fitnessOfPopulation(ArrayList<ArrayList<Integer>> population){
		HashMap<ArrayList<Integer>,Double> dictionaryOfFitness = new HashMap<ArrayList<Integer>,Double>();
		for(int i=0;i<population.size();i++){
			dictionaryOfFitness.put(population.get(i),calculateFitness(population.get(i)));
		}
		
		return dictionaryOfFitness;
	}
	
	//Pomoæna funkcija za biranje roditelja
	public ArrayList<ArrayList<Integer>> selectParents(HashMap<ArrayList<Integer>,Double> dictionaryOfFitness){
		
		ArrayList<ArrayList<Integer>> parents = new ArrayList<ArrayList<Integer>>();
		//ArrayList<Integer> parent1 = new ArrayList<Integer>();
		//ArrayList<Integer> parent2 = new ArrayList<Integer>();
		double sum = 0.0f;
		for (double f : dictionaryOfFitness.values()) {
		    sum += f;
		}
		double median=0.0;
		
		double random1=Math.random();
		double random2=Math.random();
		
		for (HashMap.Entry<ArrayList<Integer>,Double> entry : dictionaryOfFitness.entrySet()) {
			ArrayList<Integer> key = entry.getKey();
			Double value = entry.getValue();
		    // ...
			if (median<random1){
				parents.add(key);
			}
			if (median<random2){
				parents.add(key);
			}
			
			median+=value/sum;
			if (parents.size()>=2){
				break;
			}
		}
		
		
		return parents;
	}
	
	public ArrayList<Integer> returnBest(HashMap<ArrayList<Integer>,Double> dictionaryOfFitness){
		Stream<HashMap.Entry<ArrayList<Integer>,Double>> sorted = dictionaryOfFitness.entrySet().stream().sorted(Collections.reverseOrder(HashMap.Entry.comparingByValue()));
		Optional<Entry<ArrayList<Integer>, Double>> first = sorted.findFirst();
		Entry<ArrayList<Integer>, Double> val = first.get();
		return val.getKey();
	}
	
	//Križanje
	//Greedy Crossover
	public ArrayList<ArrayList<Integer>> greedyCrossover(ArrayList<Integer> parent1, ArrayList<Integer> parent2){
		ArrayList<ArrayList<Integer>> children = new ArrayList<ArrayList<Integer>>();
		HashMap<Integer,Integer> tract = new HashMap<Integer,Integer>();
		int sizeOfChild = parent1.size();
		
		//Definiranje preslikavanja pomoæu rjeènika
		for (int i=0;i<sizeOfChild;i++){
			tract.put(parent1.get(i), parent2.get(i));
		}
		
		ArrayList<Integer> child1= new ArrayList<Integer>();
		ArrayList<Integer> child2= new ArrayList<Integer>();
		
		child1.add(parent1.get(0));
		child2.add(parent2.get(0));
		
		/*
		int prevchild1_size=child1.size();   
		while(child1.size()< sizeOfChild){
			int k = child1.size();
			if (k>=sizeOfChild){
				k=1;
			}
			double child1toparent1 = euclidianDistance(dictionary.get(child1.get(child1.size()-1)),dictionary.get(parent1.get(k)));
			double child1toparent2 = euclidianDistance(dictionary.get(child1.get(child1.size()-1)),dictionary.get(parent2.get(k)));
			if (child1toparent1>child1toparent2){
				if (!child1.contains(parent1.get(child1.size()))){
					child1.add(
				}
				else{
					
				}
			}
			else{
				
			}
		}
		*/
		int k = 0;
		for(int i=0;i<sizeOfChild;i++){
			if(i+1>=sizeOfChild){
				k=0;
			}
			else{
				k = i+1;
			}
			double child1toparent1 = euclidianDistance(dictionary.get(child1.get(i)),dictionary.get(parent1.get(k)));
			double child1toparent2 = euclidianDistance(dictionary.get(child1.get(i)),dictionary.get(parent2.get(k)));
			if (child1toparent1>child1toparent2){
				if (!child1.contains(parent1.get(k))){
					child1.add(parent1.get(k));
				}
				else if(!child1.contains(parent2.get(k))){
					child1.add(parent2.get(k));
				}
				else{
					int nextchild = parent1.get(k);
					while(child1.size()==i){
						if (!child1.contains(nextchild)){
							child1.add(nextchild);
						}
						else{
							nextchild = tract.get(nextchild);
						}
					}
				}
			}
			else{
				if (!child1.contains(parent2.get(k))){
					child1.add(parent2.get(k));
				}
				else if(!child1.contains(parent1.get(k))){
					child1.add(parent1.get(k));
				}
				else{
					int nextchild = parent2.get(k);
					while(child1.size()==i){
						if (!child1.contains(nextchild)){
							child1.add(nextchild);
						}
						else{
							nextchild = tract.get(nextchild);
						}
					}
				}
			}
		}
			
		
		for(int i=0;i<sizeOfChild;i++){
			if(i+1>=sizeOfChild){
				k=0;
			}
			else{
				k = i+1;
			}
			double child1toparent1 = euclidianDistance(dictionary.get(child2.get(i)),dictionary.get(parent1.get(k)));
			double child1toparent2 = euclidianDistance(dictionary.get(child2.get(i)),dictionary.get(parent2.get(k)));
			if (child1toparent1>child1toparent2){
				if (!child2.contains(parent1.get(k))){
					child2.add(parent1.get(k));
				}
				else if(!child2.contains(parent2.get(k))){
					child2.add(parent2.get(k));
				}
				else{
					int nextchild = parent1.get(k);
					while(child2.size()==i){
						if (!child2.contains(nextchild)){
							child2.add(nextchild);
						}
						else{
							nextchild = tract.get(nextchild);
						}
					}
				}
			}
			else{
				if (!child2.contains(parent2.get(k))){
					child2.add(parent2.get(k));
				}
				else if(!child2.contains(parent1.get(k))){
					child2.add(parent1.get(k));
				}
				else{
					int nextchild = parent2.get(k);
					while(child2.size()==i){
						if (!child2.contains(nextchild)){
							child2.add(nextchild);
						}
						else{
							nextchild = tract.get(nextchild);
						}
					}
				}
			}
		}
		
		children.add(child1);
		children.add(child2);
		return children;
	}
	
	//uniform Crossover
	public ArrayList<ArrayList<Integer>> uniformCrossover(ArrayList<Integer> parent1, ArrayList<Integer> parent2){
		ArrayList<ArrayList<Integer>> children = new ArrayList<ArrayList<Integer>>();
		HashMap<Integer,Integer> tract = new HashMap<Integer,Integer>();
		int sizeOfChild = parent1.size();
		
		//Definiranje preslikavanja pomoæu rjeènika
		for (int i=0;i<sizeOfChild;i++){
			tract.put(parent1.get(i), parent2.get(i));
		}
		
		ArrayList<Integer> child1= new ArrayList<Integer>();
		ArrayList<Integer> child2= new ArrayList<Integer>();
		
		for(int i=0;i<sizeOfChild;i++){
			if (Math.random() < 0.5){
				int nextchild1 = parent1.get(i);
				while(child1.size()==i){
					if (!child1.contains(nextchild1)){
						child1.add(nextchild1);
					}
					else{
						nextchild1 = tract.get(nextchild1);
					}
				}
				int nextchild = parent2.get(i);
				while(child1.size()==i){
					if (!child2.contains(nextchild)){
						child2.add(nextchild);
					}
					else{
						nextchild = tract.get(nextchild);
					}
				}
			}
			else{
				int nextchild2 = parent2.get(i);
				while(child2.size()==i){
					if (!child2.contains(nextchild2)){
						child2.add(nextchild2);
					}
					else{
						nextchild2 = tract.get(nextchild2);
					}
				}
				int nextchild3 = parent1.get(i);
				while(child1.size()==i){
					if (!child2.contains(nextchild3)){
						child2.add(nextchild3);
					}
					else{
						nextchild3 = tract.get(nextchild3);
					}
				}
			}
		}
		
		children.add(child1);
		children.add(child2);
		return children;
	}
	
	
	//vraca veci od 2
	public int getBigger(int a, int b){
		if (a>b){
			return a;
		}
		else{
			return b;
		}
		
	}
	
	//vraca manji od 2
	public int getSmaller(int a,int b){
		if (a<b){
			return a;
		}
		else{
			return b;
		}
	}
	
	//vraca 2 random broja
	public ArrayList<Integer> getTwoNum(){
		ArrayList<Integer> ret = new ArrayList<Integer>();
		Random rn = new Random();
		int range = numberOfCities + 1;
		int randomNum1 =  rn.nextInt(range);
		int randomNum2 =  rn.nextInt(range);
		while(randomNum1==randomNum2){
			randomNum2 =  rn.nextInt(range);
		}
		int bigger = getBigger(randomNum1,randomNum2);
		int smaller = getSmaller(randomNum1,randomNum2);
		ret.add(smaller);
		ret.add(bigger);
		return ret;
	}
	
	
	//Crossover
	public ArrayList<ArrayList<Integer>> inverseCrossover(ArrayList<Integer> parent1, ArrayList<Integer> parent2){
		ArrayList<ArrayList<Integer>> children = greedyCrossover(parent1,parent2);
		ArrayList<Integer> child1=children.get(0);
		ArrayList<Integer> child2=children.get(1);
		ArrayList<Integer> invchild1=child1;
		ArrayList<Integer> invchild2=child2;
		Collections.reverse(invchild1);
		Collections.reverse(invchild2);
		
		ArrayList<Integer> lis = getTwoNum();
		int bigger = lis.get(1);
		int smaller = lis.get(0);
		for(int i = smaller; i<bigger;i++){
			child1.set(i, invchild1.get(i));
		}
		
		lis = getTwoNum();
		bigger = lis.get(1);
		smaller = lis.get(0);
		for(int i = smaller; i<bigger;i++){
			child2.set(i, invchild2.get(i));
		}
		
		children=new ArrayList<ArrayList<Integer>>();
		children.add(child1);
		children.add(child2);
		
		return children;
	}
	
	public int getBinomial() {
		  int x = 0;
		  for(int i = 0; i < numberOfCities-1; i++) {
		    if(Math.random() < mutationProbability)
		      x++;
		  }
		  return x;
		}
	
	
	public ArrayList<Integer> mutate(ArrayList<Integer> chromosome){
		int numberCity = getBinomial();
		int positionNumber = getBinomial();
		if (numberCity==numberOfCities){
			numberCity-=1;
		}
		if (positionNumber==numberOfCities){
			positionNumber-=1;
		}
		//System.out.print("index first:"+chromosome.indexOf(numberCity));
		//System.out.println("index second:"+positionNumber);
		//int tochange = chromosome.get(positionNumber);
		if (Math.random()<mutationProbability){
			Collections.swap(chromosome, positionNumber, chromosome.indexOf(numberCity));
		}
		return chromosome;
	}
		
	
	public ArrayList<Integer> greedySwapMutate(ArrayList<Integer> chromosome){
		Random rn = new Random();
		int range = numberOfCities;
		int randomPlace =  rn.nextInt(range);
		int randomCity =  rn.nextInt(range);
		int nextrandomPlace=randomPlace+1;
		int prevrandomPlace=randomPlace-1;
		if (randomPlace>=chromosome.size()-1){
			nextrandomPlace=0;
		}
		if(randomPlace==0){
			prevrandomPlace=numberOfCities-1;
		}
		
		//int tochange = chromosome.get(positionNumber);
		double old_sum1 = euclidianDistance(dictionary.get(chromosome.get(randomPlace)),dictionary.get(chromosome.get(prevrandomPlace)))+euclidianDistance(dictionary.get(chromosome.get(randomPlace)),dictionary.get(chromosome.get(nextrandomPlace)));
		double new_sum1 = euclidianDistance(dictionary.get(randomCity),dictionary.get(chromosome.get(prevrandomPlace)))+euclidianDistance(dictionary.get(randomCity),dictionary.get(chromosome.get(nextrandomPlace)));
		int place2 = chromosome.indexOf(randomCity);
		int nextplace2=place2+1;
		int prevplace2=place2-1;
		if (place2>=chromosome.size()-1){
			nextplace2=0;
		}
		else if(place2==0){
			prevplace2=numberOfCities-1;
		}
		double old_sum2 = euclidianDistance(dictionary.get(randomCity),dictionary.get(chromosome.get(prevplace2)))+euclidianDistance(dictionary.get(randomCity),dictionary.get(chromosome.get(nextplace2)));
		double new_sum2 = euclidianDistance(dictionary.get(chromosome.get(randomPlace)),dictionary.get(chromosome.get(prevplace2)))+euclidianDistance(dictionary.get(chromosome.get(randomPlace)),dictionary.get(chromosome.get(nextplace2)));
		
		if((old_sum1+old_sum2)>(new_sum1+new_sum2)){		
			Collections.swap(chromosome, randomPlace, chromosome.indexOf(randomCity));
		}
		
		return chromosome;
	}
	
	//sam algoritam HGA
	//Zasada jedini kriterij zaustavljanja broj iteracija
	public ArrayList<Point> algotihtm(){
		ArrayList<ArrayList<Integer>> population = firstPopulation();
		ArrayList<ArrayList<Integer>> old_population = null;
		HashMap<ArrayList<Integer>,Double> dictionaryOfFitness = fitnessOfPopulation(population);
		
		ArrayList<ArrayList<Integer>> new_population = null;
		int brojiteracija = 0;
		double criteria = getCriteria();
		System.out.println(criteria);
		while(brojiteracija<numIterations){
			//System.out.println(brojiteracija);
			if (brojiteracija==0){
				old_population = population;
			}
			else{
				old_population = new_population;
			}
			
			dictionaryOfFitness = fitnessOfPopulation(old_population);
			
			if(calculateFitness(returnBest(dictionaryOfFitness))>criteria){
				new_population=old_population;
				break;
			}
			new_population = new ArrayList<ArrayList<Integer>>();
			new_population.add(returnBest(dictionaryOfFitness));
			while(new_population.size()<numberOfCities){
				ArrayList<ArrayList<Integer>> parents = selectParents(dictionaryOfFitness);
				ArrayList<Integer> parent1 = parents.get(0);
				ArrayList<Integer> parent2 = parents.get(1);
				
				ArrayList<ArrayList<Integer>> children = greedyCrossover(parent1,parent2);
				ArrayList<Integer> child1=children.get(0);
				ArrayList<Integer> child2=children.get(1);
				
				child1=greedySwapMutate(child1);
				child2=greedySwapMutate(child2);
				new_population.add(child1);
				new_population.add(child2);
				
			}
			brojiteracija++;
		}
		
		//Pretvoriti natrag u tocke
		dictionaryOfFitness = fitnessOfPopulation(new_population);
		ArrayList<Point> output_cities = new ArrayList<Point>();
		ArrayList<Integer> bestChromosome = returnBest(dictionaryOfFitness);
		for(int i = 0;i<bestChromosome.size();i++){
			output_cities.add(dictionary.get(bestChromosome.get(i)));
		}
		System.out.println("Total distance to travel: "+calculateDistance(bestChromosome));
		System.out.println("Maximum distance between 2 cities: "+maxDistance);
		return output_cities;
		
	}
	
	public ArrayList<Point> greedyAlgorithmCities(){
		ArrayList<Point> output_cities = new ArrayList<Point>();
		ArrayList<Integer> bestChromosome = greedyCities();
		for(int i = 0;i<bestChromosome.size();i++){
			output_cities.add(dictionary.get(bestChromosome.get(i)));
		}
		return output_cities;
	}
	
	public double minDistanceGreedy() {
		ArrayList<Integer> list = greedyCities();
		return calculateDistance(list);
	}
	
	
}
	

