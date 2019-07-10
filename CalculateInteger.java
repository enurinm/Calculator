package Calculator;

/** 
 * 201713074 
 * 임예린
 */

import java.util.Scanner;
import static java.lang.System.out;
import java.util.EmptyStackException;
import java.util.StringTokenizer;
import java.lang.Integer;


class CalculateInteger {
	
	public static void main(String[] args) {
		// 스캔으로 문자열 입력받고 토커나이징하는 함수 호출
		Scanner scan=new Scanner(System.in);
		String s;
		out.println("수식을 입력한 후 엔터키를 누르세요. ");
		s=scan.nextLine();
		insertStack(s);	
	}

	private static void insertStack(String s2) {
		// 토커나이징하며 스택에 입력. 계산 호출
		
		ListStack lnum=new ListStack();
		ListStack lopr=new ListStack();
		Calculator cl=new Calculator();	
		
		String numopr = new String("");		
		String stall[]=s2.split("");
		int num=-1; //숫자 받아서 넣어주는 애
		
		for(int i=0;i<stall.length;i++) {
			if((stall[i]).equals(" ")) {	continue;	}
			
			if((stall[i]).equals("+")||(stall[i]).equals("-")) {	
				if(!numopr.equals("")) {										
					lnum.push(numopr);
					numopr=""; //현 연산자가 나오기 이전에 저장된 숫자를 리스트에 넣음
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
		
		out.println("답: "+lnum.top.getItem());
		
	}	
			
}

class Calculator{				
	
	public void calc(ListStack lnum, ListStack lopr) {
		// 숫자 앞에 오는 연산자에 따라 연산 함수 호출
		if(lopr.isEmpty()) {return;}
		
		if(lopr.top.getItem().toString().equals("(")) { //할거없음 그냥 리턴할것
			return;
		}
		else if(lopr.top.getItem().toString().equals("*") || lopr.top.getItem().toString().equals("/")) {
			muldiv(lnum,lopr);
		}
		else { //+나 -일때
			sumsubcheck(lnum,lopr);
		}
		return;
	}

	private void sumsubcheck(ListStack lnum, ListStack lopr) {
		// 앞부분 체크 및 뒷부분 체크 호출
		String num1="";
		String oprbump=lopr.pop().toString();//현재의 +- 잠깐 빼두고		
		
		if(lopr.isEmpty()) {//앞에 연산자가 없으면 더하기
			lopr.push(oprbump);
			sumsub(lnum,lopr);
		}
		else if(lopr.top.getItem().toString().equals("*") || lopr.top.getItem().toString().equals("/")) {//*/면 앞을 먼저 계산		
			num1=lnum.pop().toString();
			muldiv(lnum,lopr);
			lopr.push(oprbump);//연산 후 팝해뒀던 연산자와 숫자 푸시
			lnum.push(num1);
		}
		else {//+면 다시 넣고 현재 계산
			lopr.push(oprbump);
			sumsub(lnum,lopr);
		}	
		return;
	}

	public void bracket(ListStack lnum, ListStack lopr) {
		// 끝 괄호를 만났을 때 첫 괄호가 나올 때 까지 연산 반복
		while(!lopr.peek().equals("(")) {
			calc(lnum,lopr);			
		}
		lopr.pop();
		return;
	}

	public void muldiv(ListStack lnum, ListStack lopr) {
		// 곱하기, 나누기 연산
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
		lopr.pop(); //사용한 연산자(*/)제거
		return;
	}

	public void sumsub(ListStack lnum, ListStack lopr) {
		// 더하기, 빼기 연산, 차례대로 계산할 수 있도록
		String num1="";
		String num2="";
		int ans=-1;
		String oprbump=lopr.pop().toString();
		
		if(lopr.isEmpty()) {//앞에 연산자가 없으면 더하기
			lopr.push(oprbump);
			subsumhelp(lnum,lopr,oprbump);	
		}
		else if(lopr.top.getItem().toString().equals("-")) {//*/면 앞을 먼저 계산		
			String num=lnum.pop().toString();
			num1=lnum.pop().toString();
			num2=lnum.pop().toString();
			ans=Integer.parseInt(num2)-Integer.parseInt(num1);
			lnum.push(ans);
			lopr.pop();
			lopr.push(oprbump);//연산 후 팝해뒀던 연산자와 숫자 푸시
			lnum.push(num);
		} 
		else{
			lopr.push(oprbump);
			subsumhelp(lnum,lopr,oprbump);
		}								
		return;
							
	}

	private void subsumhelp(ListStack lnum, ListStack lopr, String oprbump) {
		// 실질적 더하기, 빼기 연산 수행
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



class Node<E> { //노드
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


class ListStack<E>{ //스택
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

