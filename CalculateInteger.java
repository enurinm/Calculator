package Calculator;

/** 
 * 201713074 
 * �ӿ���
 */

import java.util.Scanner;
import static java.lang.System.out;
import java.util.EmptyStackException;
import java.util.StringTokenizer;
import java.lang.Integer;


class CalculateInteger {
	
	public static void main(String[] args) {
		// ��ĵ���� ���ڿ� �Է¹ް� ��Ŀ����¡�ϴ� �Լ� ȣ��
		Scanner scan=new Scanner(System.in);
		String s;
		out.println("������ �Է��� �� ����Ű�� ��������. ");
		s=scan.nextLine();
		insertStack(s);	
	}

	private static void insertStack(String s2) {
		// ��Ŀ����¡�ϸ� ���ÿ� �Է�. ��� ȣ��
		
		ListStack lnum=new ListStack();
		ListStack lopr=new ListStack();
		Calculator cl=new Calculator();	
		
		String numopr = new String("");		
		String stall[]=s2.split("");
		int num=-1; //���� �޾Ƽ� �־��ִ� ��
		
		for(int i=0;i<stall.length;i++) {
			if((stall[i]).equals(" ")) {	continue;	}
			
			if((stall[i]).equals("+")||(stall[i]).equals("-")) {	
				if(!numopr.equals("")) {										
					lnum.push(numopr);
					numopr=""; //�� �����ڰ� ������ ������ ����� ���ڸ� ����Ʈ�� ����
					cl.calc(lnum, lopr);
				}
				lopr.push(stall[i]);
			}
			else if((stall[i]).equals("*")||(stall[i]).equals("/")) {	
				if(!numopr.equals("")) {
					lnum.push(numopr);
					numopr="";						
				}
				lopr.push(stall[i]);				
			}
			else if((stall[i]).equals("(")) {	
				if(!numopr.equals("")) {
					lnum.push(numopr);
					numopr="";
				}
				lopr.push(stall[i]);
			}
			else if((stall[i]).equals(")")) {	
				if(!numopr.equals("")) {
					lnum.push(numopr);
					numopr="";					
					cl.bracket(lnum, lopr);
				}
			}
			else {
				numopr=numopr.concat(stall[i]);
			}
		}
		
		if(!numopr.equals("")) {
			lnum.push(numopr);
			cl.calc(lnum, lopr);
		}
		
		while(lnum.size()>1) {
			cl.calc(lnum, lopr);
		}
		
		out.println("��: "+lnum.top.getItem());
		
	}	
			
}

class Calculator{				
	
	public void calc(ListStack lnum, ListStack lopr) {
		// ���� �տ� ���� �����ڿ� ���� ���� �Լ� ȣ��
		if(lopr.isEmpty()) {return;}
		
		if(lopr.top.getItem().toString().equals("(")) { //�Ұž��� �׳� �����Ұ�
			return;
		}
		else if(lopr.top.getItem().toString().equals("*") || lopr.top.getItem().toString().equals("/")) {
			muldiv(lnum,lopr);
		}
		else { //+�� -�϶�
			sumsubcheck(lnum,lopr);
		}
		return;
	}

	private void sumsubcheck(ListStack lnum, ListStack lopr) {
		// �պκ� üũ �� �޺κ� üũ ȣ��
		String num1="";
		String oprbump=lopr.pop().toString();//������ +- ��� ���ΰ�		
		
		if(lopr.isEmpty()) {//�տ� �����ڰ� ������ ���ϱ�
			lopr.push(oprbump);
			sumsub(lnum,lopr);
		}
		else if(lopr.top.getItem().toString().equals("*") || lopr.top.getItem().toString().equals("/")) {//*/�� ���� ���� ���		
			num1=lnum.pop().toString();
			muldiv(lnum,lopr);
			lopr.push(oprbump);//���� �� ���ص״� �����ڿ� ���� Ǫ��
			lnum.push(num1);
		}
		else {//+�� �ٽ� �ְ� ���� ���
			lopr.push(oprbump);
			sumsub(lnum,lopr);
		}	
		return;
	}

	public void bracket(ListStack lnum, ListStack lopr) {
		// �� ��ȣ�� ������ �� ù ��ȣ�� ���� �� ���� ���� �ݺ�
		while(!lopr.peek().equals("(")) {
			calc(lnum,lopr);			
		}
		lopr.pop();
		return;
	}

	public void muldiv(ListStack lnum, ListStack lopr) {
		// ���ϱ�, ������ ����
		String num1="";
		String num2="";
		int ans=-1;
		
		if(lopr.top.getItem().toString().equals("*")) {
			num1=lnum.pop().toString();
			num2=lnum.pop().toString();
			ans=Integer.parseInt(num1)*Integer.parseInt(num2);
			lnum.push(ans);
		}
		else if(lopr.top.getItem().toString().equals("/")){ //operator=="/"
			num1=lnum.pop().toString();
			num2=lnum.pop().toString();
			ans=Integer.parseInt(num2)/Integer.parseInt(num1);
			lnum.push(ans);
		}
		lopr.pop(); //����� ������(*/)����
		return;
	}

	public void sumsub(ListStack lnum, ListStack lopr) {
		// ���ϱ�, ���� ����, ���ʴ�� ����� �� �ֵ���
		String num1="";
		String num2="";
		int ans=-1;
		String oprbump=lopr.pop().toString();
		
		if(lopr.isEmpty()) {//�տ� �����ڰ� ������ ���ϱ�
			lopr.push(oprbump);
			subsumhelp(lnum,lopr,oprbump);	
		}
		else if(lopr.top.getItem().toString().equals("-")) {//*/�� ���� ���� ���		
			String num=lnum.pop().toString();
			num1=lnum.pop().toString();
			num2=lnum.pop().toString();
			ans=Integer.parseInt(num2)-Integer.parseInt(num1);
			lnum.push(ans);
			lopr.pop();
			lopr.push(oprbump);//���� �� ���ص״� �����ڿ� ���� Ǫ��
			lnum.push(num);
		} 
		else{
			lopr.push(oprbump);
			subsumhelp(lnum,lopr,oprbump);
		}								
		return;
							
	}

	private void subsumhelp(ListStack lnum, ListStack lopr, String oprbump) {
		// ������ ���ϱ�, ���� ���� ����
		String num1="";
		String num2="";
		int ans=-1;

		if(lopr.top.getItem().toString().equals("+")) {
			num1=lnum.pop().toString();
			num2=lnum.pop().toString();
			ans=Integer.parseInt(num1)+Integer.parseInt(num2);
			lnum.push(ans);
			lopr.pop();
		}
		else if(lopr.top.getItem().toString().equals("-")) { //operator=="-"
			num1=lnum.pop().toString();
			num2=lnum.pop().toString();
			ans=Integer.parseInt(num2)-Integer.parseInt(num1);
			lnum.push(ans);
			lopr.pop();
		}	
	}
	
}



class Node<E> { //���
	private E item;
	private Node<E> next;
	public Node(E newItem, Node<E> node) {
		item=newItem;
		next=node;
	}
	
	public E getItem() {	return item;	}
	public Node<E> getNext(){	return next;	}
	public void setItem(E newItem) {	item=newItem;	}
	public void setNext(Node<E> newNext) {	next=newNext;	}
}


class ListStack<E>{ //����
	protected Node top;
	private int size;	
	public ListStack() {
		top=null;
		size=0;
	}
	public int size() {return size;}
	boolean isEmpty() {	return size==0;	}
	
	public E peek() {
		if(isEmpty()) throw new EmptyStackException();
		return (E) top.getItem();
	}
	
	public void push(E newItem) {
		Node newNode=new Node(newItem,top);
		top=newNode;
		size++;
	}
	
	public E pop() {
		if(isEmpty()) throw new EmptyStackException();
		
		E topItem=(E) top.getItem();
		top=top.getNext();
		size--;
		
		return topItem;
	}
}

