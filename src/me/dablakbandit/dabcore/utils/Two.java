package me.dablakbandit.dabcore.utils;

import java.util.Objects;

public class Two<T, U>{
	
	public T	t;
	public U	u;
	
	public Two(T t, U u){
		this.t = t;
		this.u = u;
	}
	
	public int hashCode(){
		return Objects.hash(t, u);
	}
	
	@SuppressWarnings("rawtypes")
	public boolean equals(Object obj){
		if((obj != null) && ((obj instanceof Two))){
			Two two = (Two)obj;
			return two.t.equals(t) && two.u.equals(u);
		}
		return false;
	}
	
	public String toString(){
		return String.format("%s(%s),%s(%s)", t.getClass(), t.toString(), u.getClass(), u.toString());
	}
	
	public T getFirst(){
		return t;
	}
	
	public U getSecond(){
		return u;
	}
}
