package me.dablakbandit.dabcore.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.bukkit.Bukkit;

public class NMSUtils{

	private static String version = getVersion();

	public static String getVersion(){
		if(version!=null)return version;
		String name = Bukkit.getServer().getClass().getPackage().getName();
		return name.substring(name.lastIndexOf('.') + 1) + ".";
	}

	public static Class<?> getNMSClassWithException(String className) throws Exception{
		return Class.forName("net.minecraft.server." + getVersion() + className);
	}

	public static Class<?> getNMSClass(String className){
		try{
			return getNMSClassWithException(className);
		}catch(Exception e){e.printStackTrace();}
		return null;
	}

	public static Class<?> getNMSClassSilent(String className){
		try{
			return getNMSClassWithException(className);
		}catch(Exception e){}
		return null;
	}

	public static Class<?> getNMSClass(String className, String embedded){
		try{
			return getNMSClassWithException(className);
		}catch(Exception e){
			return getInnerClass(getNMSClass(embedded), className);
		}
	}

	public static Class<?> getNMSClassSilent(String className, String embedded){
		try{
			return getNMSClassWithException(className);
		}catch(Exception e){
			return getInnerClassSilent(getNMSClass(embedded), className);
		}
	}

	public static Class<?> getOBCClassWithException(String className) throws Exception{
		return Class.forName("org.bukkit.craftbukkit." + getVersion() + className);
	}

	public static Class<?> getOBCClass(String className){
		try{
			return getOBCClassWithException(className);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static Class<?> getOBCClassSilent(String className){
		try{
			return getOBCClassWithException(className);
		}catch(Exception e){}
		return null;
	}

	public static Object getHandle(Object obj){
		try{
			return getMethod(obj.getClass(), "getHandle").invoke(obj);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static Object getHandleSilent(Object obj){
		try{
			return getMethod(obj.getClass(), "getHandle").invoke(obj);
		}catch(Exception e){
			return null;
		}
	}

	private static Class<?> c = getOBCClass("block.CraftBlock");
	private static Method m = getMethod(c, "getNMSBlock");

	public static Object getBlockHandleWithException(Object obj) throws Exception{
		return m.invoke(obj);
	}

	public static Object getBlockHandle(Object obj){
		try{
			return m.invoke(obj);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static Object getBlockHandleSilent(Object obj){
		try{
			return m.invoke(obj);
		}catch(Exception e){
			return null;
		}
	}

	public static Field getFieldWithException(Class<?> clazz, String name) throws Exception{
		for(Field field : clazz.getDeclaredFields())
			if(field.getName().equals(name)){
				field.setAccessible(true);
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				int modifiers = modifiersField.getInt(field);
				modifiers &= ~Modifier.FINAL;
				modifiersField.setInt(field, modifiers);
				return field;
			}
		for(Field field : clazz.getFields())
			if(field.getName().equals(name)){
				field.setAccessible(true);
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				int modifiers = modifiersField.getInt(field);
				modifiers &= ~Modifier.FINAL;
				modifiersField.setInt(field, modifiers);
				return field;
			}
		throw new Exception("Field Not Found");
	}

	public static Field getField(Class<?> clazz, String name){
		try{
			return getFieldWithException(clazz, name);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static Field getFieldSilent(Class<?> clazz, String name){
		try{
			return getFieldWithException(clazz, name);
		}catch(Exception e){}
		return null;
	}

	public static Field getFieldOfTypeWithException(Class<?> clazz, Class<?> type, String name) throws Exception{
		for(Field field : clazz.getDeclaredFields())
			if(field.getName().equals(name)&&field.getType().equals(type)){
				field.setAccessible(true);
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				int modifiers = modifiersField.getInt(field);
				modifiers &= ~Modifier.FINAL;
				modifiersField.setInt(field, modifiers);
				return field;
			}
		for(Field field : clazz.getFields())
			if(field.getName().equals(name)&&field.getType().equals(type)){
				field.setAccessible(true);
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				int modifiers = modifiersField.getInt(field);
				modifiers &= ~Modifier.FINAL;
				modifiersField.setInt(field, modifiers);
				return field;
			}
		throw new Exception("Field Not Found");
	}

	public static Field getFieldOfType(Class<?> clazz, Class<?> type, String name){
		try{
			return getFieldOfTypeWithException(clazz, type, name);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static Field getFirstFieldOfTypeWithException(Class<?> clazz, Class<?> type) throws Exception{
		for(Field field : clazz.getDeclaredFields())
			if(field.getType().equals(type)){
				field.setAccessible(true);
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				int modifiers = modifiersField.getInt(field);
				modifiers &= ~Modifier.FINAL;
				modifiersField.setInt(field, modifiers);
				return field;
			}
		throw new Exception("Field Not Found");
	}

	public static Field getFirstFieldOfType(Class<?> clazz, Class<?> type){
		try{
			return getFirstFieldOfTypeWithException(clazz, type);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static Field getLastFieldOfTypeWithException(Class<?> clazz, Class<?> type) throws Exception{
		Field field = null;
		for(Field f : clazz.getDeclaredFields())
			if(f.getType().equals(type)){
				field = f;
			}
		if(field==null){
			throw new Exception("Field Not Found");
		}
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		int modifiers = modifiersField.getInt(field);
		modifiers &= ~Modifier.FINAL;
		modifiersField.setInt(field, modifiers);
		return field;
	}

	public static Field getLastFieldOfType(Class<?> clazz, Class<?> type){
		try{
			return getLastFieldOfTypeWithException(clazz, type);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static Method getMethod(Class<?> clazz, String name,	Class<?>... args){
		for(Method m : clazz.getDeclaredMethods())
			if(m.getName().equals(name) &&(args.length == 0 && m.getParameterTypes().length == 0 || ClassListEqual(args, m.getParameterTypes()))){
				m.setAccessible(true);
				return m;
			}
		for(Method m : clazz.getMethods())
			if(m.getName().equals(name) &&(args.length == 0 && m.getParameterTypes().length == 0 || ClassListEqual(args, m.getParameterTypes()))){
				m.setAccessible(true);
				return m;
			}
		return null;
	}

	public static Method getMethodSilent(Class<?> clazz, String name, Class<?>... args){
		try{
			return getMethod(clazz, name, args);
		}catch(Exception e){}
		return null;
	}

	public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2){
		if(l1.length != l2.length)return false;
		for(int i = 0; i < l1.length; i++)
			if(l1[i] != l2[i])return false;
		return true;
	}

	public static Class<?> getInnerClassWithException(Class<?> c, String  className) throws Exception{
		for(Class<?> cl : c.getDeclaredClasses())
			if(cl.getSimpleName().equals(className))return cl;
		return null;
	}

	public static Class<?> getInnerClass(Class<?> c, String className){
		try{
			return getInnerClassWithException(c, className);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static Class<?> getInnerClassSilent(Class<?> c, String className){
		try{
			return getInnerClassWithException(c, className);
		}catch(Exception e){}
		return null;
	}

	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... args){
		for(Constructor<?> c : clazz.getDeclaredConstructors())
			if(args.length == 0 && c.getParameterTypes().length == 0 || ClassListEqual(args, c.getParameterTypes())){
				c.setAccessible(true);
				return c;
			}
		for(Constructor<?> c : clazz.getConstructors())
			if(args.length == 0 && c.getParameterTypes().length == 0 || ClassListEqual(args, c.getParameterTypes())){
				c.setAccessible(true);
				return c;
			}
		return null;
	}

	public static Constructor<?> getConstructorSilent(Class<?> clazz, Class<?>... args){
		try{
			return getConstructor(clazz, args);
		}catch(Exception e){}
		return null;
	}

}
