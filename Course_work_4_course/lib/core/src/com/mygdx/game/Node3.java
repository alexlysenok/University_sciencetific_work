package com.mygdx.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

public class Node3 extends NewNode implements INodeBehaviour, Telegraph {

	private boolean haveArrived = false;
	private static final int FLAG_INDEX_CODE = 1;
	private static final int NODE_COST_CODE = 2;
	private static final int ACTIONEER_ANSWER_CODE = 3;
	private int flagIndex = 0;
	Map<Integer, Float> prices = new HashMap<Integer, Float>();
	private static List<NewNode> freeNodes = new ArrayList<NewNode>();

	private int index = 1;
	
	
	public boolean mustIncrease=false;

	public float incriment=0f;
	
	public Node3(float startX, float startY, int id) {
		

		
		if(id==0){
			
			texture = new Texture(Gdx.files.internal("node2.png"));
		}
		
		dispatcher.addListener(this, 1);

		this.startX = startX;
		this.startY = startY;
		myXx = startX;
		myYy = startY;
		this.id = id;

		freeNodes.add(this);

	}

	@Override
	public void draw(Batch batch, float alpha) {
		batch.draw(texture, myXx, myYy);
	}

	@Override
	public void act(float delta) {
		if (timeToGo) {

			if (myXx > xDestination) // ����������� �� �
			{
				nextX = myXx - speedX;
			} else if (myXx < xDestination) // ����������� �� �
			{
				nextX = myXx + speedX;
			}
			if (myYy > yDestination) // ����������� �� �
			{
				nextY = myYy - speedY;
			} else if (myYy < yDestination) // ����������� �� �
			{
				nextY = myYy + speedY;
			}
			myXx = nextX;
			myYy = nextY;

			if (Math.abs(myXx - xDestination) <= speedX && Math.abs(myYy - yDestination) <= speedY) {
				stop();
				TestGame.flags.get(destinationFlagIndex).counter.stopped = true;
				TestGame.flags.get(destinationFlagIndex).flag=new Texture(Gdx.files.internal("flagTest.png"));
				freeNodes.add(this);
				TestGame.nodesList.get(0).startAuction();
				
			}

			
			
			// goToFlags();


		}
	}

	public void startAuction() {
		if (flagIndex <=TestGame.flags.size() - 1) {
		if (id == 0) {
			 System.out.println("");
			System.out.println("**********�������� ������� �� ���� #"+flagIndex+"**************");
			for (int i = 0; i < freeNodes.size(); i++) {
				
				int realIndex=TestGame.nodesList.indexOf(freeNodes.get(i));
				System.out.println("��������� ������ ���� #"+realIndex+" �� ������� � ��������"); 
				dispatcher.dispatchMessage(0, TestGame.nodesList.get(0),freeNodes.get(i), FLAG_INDEX_CODE,new Integer[] { flagIndex });
               
			}
		}}
	}

	public void goToFlags() { // ������������� ��������: ����� ����������,
								// ��������

		flagX = TestGame.flags.get(destinationFlagIndex).getX()+ TestGame.flags.get(destinationFlagIndex).flag.getWidth() / 2 * TestGame.flags.get(destinationFlagIndex).getScaleX();
		flagY = TestGame.flags.get(destinationFlagIndex).getY();
		float ratio = Math.abs(TestGame.flags.get(destinationFlagIndex).getX()
				- myXx)
				/ Math.abs(TestGame.flags.get(destinationFlagIndex).getY()
						- myYy);
		xDestination = flagX-10;
		yDestination = flagY;
		speedX = 1f;
		speedY = speedX / ratio;

		if(mustIncrease){
		speedX+=speedX*incriment;
			speedY = speedX / ratio;
		}
   
	}

	@Override
	public boolean handleMessage(Telegram msg) {

		switch (msg.message) {
		case FLAG_INDEX_CODE:

			
			
			int flagId = (((Integer[]) msg.extraInfo)[0]);
			int index = (int) flagId;
			
			
			
			System.out.println("���� #"+id+" ��������� � �������� �� ���� #"+ index);
			
			
			
			float way1 = (float) Math.pow(
					Math.abs(TestGame.flags.get(index).getX() - myXx), 2);
			float way2 = (float) Math.pow(
					Math.abs(TestGame.flags.get(index).getY() - myYy), 2);
			float way = (float) Math.sqrt(way1 + way2);

			float totalPrice = price * way;

		//	System.out.println((((Integer[]) msg.extraInfo)[0]) + " " + way);

			dispatcher.dispatchMessage(0, TestGame.nodesList.get(id),msg.sender, NODE_COST_CODE, new Float[] { (float) id,totalPrice, (float) flagId });

			break;
		case NODE_COST_CODE:

			// System.out.println("go!");

			float flagId2 = (((Float[]) msg.extraInfo)[2]);
			int flagID=(int)flagId2;
			

			float nodeId = (((Float[]) msg.extraInfo)[0]);
			int nodeIndex = (int) nodeId;

			
			
			prices.put(nodeIndex, (((Float[]) msg.extraInfo)[1]));

			if (prices.size() == (freeNodes.size())) {

				Iterator<Float> it = prices.values().iterator();
				float minPrice = it.next();
				while (it.hasNext()) {
					float price = it.next();
					if (price < minPrice) {
						minPrice = price;
					}
				}

				
				
				
				
				Integer key = 0;
				Iterator<Map.Entry<Integer, Float>> iter = prices.entrySet()
						.iterator();
				while (iter.hasNext()) {
					Map.Entry<Integer, Float> entry = iter.next();
					if (entry.getValue().equals(minPrice)) {
						key = entry.getKey();

					}
				}
				
				

				TestGame.nodesList.get(key).flagX = TestGame.flags.get(flagID).getX()+ TestGame.flags.get(flagID).flag.getWidth() / 2 * TestGame.flags.get(flagID).getScaleX();
				TestGame.nodesList.get(key).flagY = TestGame.flags.get(flagID).getY();
				float ratio = Math.abs(TestGame.flags.get(flagID).getX()
						- TestGame.nodesList.get(key).myXx)
						/ Math.abs(TestGame.flags.get(flagID).getY()
								- TestGame.nodesList.get(key).myYy);
				TestGame.nodesList.get(key).xDestination = flagX-10;
				TestGame.nodesList.get(key).yDestination = flagY;
				TestGame.nodesList.get(key).speedX = 1.5f;
				TestGame.nodesList.get(key).speedY = speedX / ratio;
				
				
				
				
				
				
				float way3=(float) Math.sqrt(Math.pow(Math.abs(TestGame.flags.get(flagID).getX()-TestGame.nodesList.get(key).myXx), 2)+Math.pow(Math.abs(TestGame.flags.get(flagID).getY()-TestGame.nodesList.get(key).myYy), 2));
			    float speed3=(float)( Math.sqrt(Math.pow(TestGame.nodesList.get(key).speedX,2)+Math.pow(TestGame.nodesList.get(key).speedY, 2))*1000f/16f);
				float time3=way3/speed3;

				float wtf=TestGame.flags.get(flagID).counter.currentTime-time3;
				
				System.out.println(wtf);
				
				float difference=0f;
				
				if(wtf<0){
					
					difference=Math.abs(wtf)/time3;
					System.out.println(difference);
					
				}
								
			//	System.out.println(way3);
			//	System.out.println(speed3);
			//	System.out.println(time3);
				
				dispatcher.dispatchMessage(0, TestGame.nodesList.get(0),TestGame.nodesList.get(key), ACTIONEER_ANSWER_CODE,new Float[] { flagId2, difference });

				if (flagIndex <= TestGame.flags.size() - 1) {
					freeNodes.remove(TestGame.nodesList.get(key));
					

					TestGame.freeFlags.remove(TestGame.flags.get(flagIndex));

					flagIndex++;
					prices.clear();
					startAuction();
				}
			}

			break;
		case ACTIONEER_ANSWER_CODE:

			
			float flagId3 = (((Float[]) msg.extraInfo)[0]);
			int index3 = (int) flagId3;
			
			System.out.println("------node #" + id+" is going to the flag #"+ index3+"--------");


			if((((Float[]) msg.extraInfo)[1])!=0f){
				
				mustIncrease=true;
				incriment=(((Float[]) msg.extraInfo)[1])+0.5f;
			}
			
			destinationFlagIndex = index3;

			start();
			TestGame.startMoving = true;

			break;

		}

		/*
		 
		 
		 float f=(((Float[])msg.extraInfo)[0]); int index=(int)f;
		 
		 
		 if(msg.message==1){} if(id!=0){}

		 System.out.println( "Node #"+index+" moves from  x:"+(((Float[])msg.extraInfo)[5])+" and y:"+(((Float[])msg.extraInfo)[6])+" to x:"+(((Float[])msg.extraInfo)[1])+" and y:"+(((Float[])msg.extraInfo)[2])+ " with speed:"+(((Float[])msg.extraInfo)[3])+" and "+(((Float[])msg.extraInfo)[4]));
		  
		  for(int i=1;i<TestGame.nodesList.size();i++){ 
		  if(index==i){ 
		  floatway1= (float)Math.pow(Math.abs((((Float[])msg.extraInfo)[1])-(((Float [])msg.extraInfo)[5])), 2); 
		  float way2= (float) Math.pow(Math.abs((((Float[])msg.extraInfo)[2])-(((Float[])msg.extraInfo)[6])), 2); 
          float way =(float) Math.sqrt(way1+way2);
		  
		  
		  float speed1= (float) Math.pow(Math.abs(((Float[])msg.extraInfo)[3]),2); 
		  float speed2= (float) Math.pow(Math.abs(((Float[])msg.extraInfo)[4]), 2);
		  
		  float speed=(float) Math.sqrt(speed1+speed2);
		  
		 
		  float time=way/speed; System.out.println(time);
		 
		  //System.out.println("#"+i+" must go "+way+" pixels");
		 //System.out.println("#"+i+" will get flag in "+time+" seconds");
		  
		  dispatcher.dispatchMessage(0, msg.sender,TestGame.nodesList.get(index), NODE_COST_CODE, new Float[]{(float) index, time});
		  
		  }}
  
		  float time2=(((Float[])msg.extraInfo)[1]); System.out.println(time2 + " "+ (((Float[])msg.extraInfo)[0]));
		 */
		return false;

	}



	public void start() {
		timeToGo = true;
		goToFlags();
	}

	@Override
	public void stop() {
		xDestination = myXx;
		yDestination = myYy;

	}

}