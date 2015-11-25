package me.dablakbandit.dabcore.configuration;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public abstract class PluginConfiguration {

	protected Plugin plugin;

	public PluginConfiguration(Plugin plugin){
		this.plugin = plugin;
	}

	public static abstract class Path{

		protected String path, old;

		private Path(String path){
			this.path = path;
		}

		private Path(String path, String old){
			this(path);
			this.old = old;
		}

		public abstract boolean retrieve(FileConfiguration config);

	}

	public static class BooleanPath extends Path{

		private boolean b, def;

		public BooleanPath(String path, boolean def){
			super(path);
			this.def = def;
		}

		public BooleanPath(String path, String old, boolean def){
			super(path, old);
			this.def = def;
		}

		@Override
		public boolean retrieve(FileConfiguration config) {
			if(old!=null){
				if(config.isSet(old)){
					b = config.getBoolean(old);
					config.set(old, null);
					config.set(path, def);
					return true;
				}
			}
			if(config.isSet(path)){
				b = config.getBoolean(path);
				return false;
			}else{
				b = def;
				config.set(path, def);
				return true;
			}
		}

		public boolean get(){
			return b;
		}
	}

	public static class StringPath extends Path{

		private String s, def;

		public StringPath(String path, String def){
			super(path);
			this.def = def;
		}

		public StringPath(String path, String old, String def){
			super(path, old);
			this.def = def;
		}

		@Override
		public boolean retrieve(FileConfiguration config) {
			if(old!=null){
				if(config.isSet(old)){
					s = config.getString(old);
					config.set(old, null);
					config.set(path, def);
					return true;
				}
			}
			if(config.isSet(path)){
				s = config.getString(path);
				return false;
			}else{
				s = def;
				config.set(path, def);
				return true;
			}
		}

		public String get(){
			return s;
		}
	}

	public static class IntegerPath extends Path{

		private int i, def;

		public IntegerPath(String path, int def){
			super(path);
			this.def = def;
		}

		public IntegerPath(String path, String old, int def){
			super(path, old);
			this.def = def;
		}

		@Override
		public boolean retrieve(FileConfiguration config) {
			if(old!=null){
				if(config.isSet(old)){
					i = config.getInt(old);
					config.set(old, null);
					config.set(path, def);
					return true;
				}
			}
			if(config.isSet(path)){
				i = config.getInt(path);
				return false;
			}else{
				i = def;
				config.set(path, def);
				return true;
			}
		}

		public int get(){
			return i;
		}
	}

	public static class DoublePath extends Path{

		private double d, def;

		public DoublePath(String path, double def){
			super(path);
			this.def = def;
		}

		public DoublePath(String path, String old, double def){
			super(path, old);
			this.def = def;
		}

		@Override
		public boolean retrieve(FileConfiguration config) {
			if(old!=null){
				if(config.isSet(old)){
					d = config.getDouble(old);
					config.set(old, null);
					config.set(path, def);
					return true;
				}
			}
			if(config.isSet(path)){
				d = config.getDouble(path);
				return false;
			}else{
				d = def;
				config.set(path, def);
				return true;
			}
		}

		public double get(){
			return d;
		}
	}	

	public static class StringListPath extends Path{

		private List<String> l, def;

		public StringListPath(String path, List<String> def){
			super(path);
			this.def = def;
		}

		public StringListPath(String path, String old, List<String> def){
			super(path, old);
			this.def = def;
		}

		@Override
		public boolean retrieve(FileConfiguration config) {
			if(old!=null){
				if(config.isSet(old)){
					l = new ArrayList<String>();
					l.addAll(config.getStringList(old));
					config.set(old, null);
					config.set(path, def);
					return true;
				}
			}
			if(config.isSet(path)){
				l = new ArrayList<String>();
				l.addAll(config.getStringList(path));
				return false;
			}else{
				l = new ArrayList<String>();
				l.addAll(def);
				config.set(path, def);
				return true;
			}
		}

		public List<String> get(){
			return l;
		}
	}	
}
