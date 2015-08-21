package io.github.cybernetic_shadow.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * A Configuration loader that supports getting and setting of values in a configuration file.
 * The Configuration is backed by a Properties list and supports getting and setting of
 * primitive type data. The ConfigType enum contains the supported formats.
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author Josh "Shadow"
 * @see java.util.Properties
 * @see com.restoration_gaming.ucgo.util.configuration.ConfigType
 *
 */
public class Configuration {

	/**
	 * The properties list that handles basic saving and loading of the INI and XML types
	 * as well as stored the data in a list for easy access.
	 */
	private Properties properties;
	
	/**
	 * The Configuration file.
	 */
	private File file;
	
	/**
	 * Load a configuration file and prepare it for use.
	 * 
	 * @param type The type of configuration the file is.
	 * @param file The Configuration File.
	 * @since 1.0.0
	 * @see java.io.File
	 * @see com.restoration_gaming.ucgo.util.configuration.ConfigType
	 */
	public Configuration(File file) {
		this.file = file;
		
		this.load();
	}
	
	/**
	 * Loads the file to the properties variable. This is also able to reload the configuration
	 * file however any changes made will be lost if not saved back to the file first.
	 * 
	 * @since 1.0.0
	 */
	public void load() {
		
		properties = new Properties();
		try {
			properties.load(new FileInputStream(file));
		} catch (Exception e) {
			// TODO Add Logger
			e.printStackTrace();
		}
	}
	
	/**
	 * Save the current properties list back to the configuration file.
	 * 
	 * @param comment A comment to be added to the top of the file.
	 * @since 1.0.0
	 */
	public void save(String comment) {
		try {
			properties.store(new FileOutputStream(file), comment);
		} catch(Exception e) {
			// TODO add logger
			e.printStackTrace();
		}
	}
	
	/**
	 * The Configuration File.
	 * 
	 * @return The Configuration File
	 * @since 1.0.0
	 * @see java.io.File
	 */
	public File getFile() {
		return this.file;
	}
	
	/**
	 * Get the String associated with the given key.
	 * 
	 * @param key The key of the value to get.
	 * @param defaultValue The default value to use and store if no value is found.
	 * @return The gotten String.
	 * @since 1.0.0
	 */
	public String getString(String key, String defaultValue) {
		String value = this.properties.getProperty(key);
		if(value == null) {
			this.setString(key, defaultValue);
			return defaultValue;
		} 
		
		return value;
	}
	
	/**
	 * Set the value of the given key.
	 * 
	 * @param key The key to be set.
	 * @param value The value for the key to be set to.
	 * @since 1.0.0
	 */
	public void setString(String key, String value) {
		properties.setProperty(key, value);
	}
	
	/**
	 * Get the boolean associated with the given key.
	 * 
	 * @param key The key of the value to get.
	 * @param defaultValue The default value to use and store if no value is found.
	 * @return The gotten boolean.
	 * @since 1.0.0
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		return Boolean.parseBoolean(this.getString(key, "" + defaultValue));
	}
	
	/**
	 * Set the value of the given key.
	 * 
	 * @param key The key to be set.
	 * @param value The value for the key to be set to.
	 * @since 1.0.0
	 */
	public void setBoolean(String key, boolean value) {
		this.setString(key, "" + value);
	}
	
	/**
	 * Get the byte associated with the given key.
	 * 
	 * @param key The key of the value to get.
	 * @param defaultValue The default value to use and store if no value is found.
	 * @return The gotten byte.
	 * @since 1.0.0
	 */
	public byte getByte(String key, byte defaultValue) {
		return Byte.parseByte(this.getString(key, "" + defaultValue));
	}
	
	/**
	 * Set the value of the given key.
	 * 
	 * @param key The key to be set.
	 * @param value The value for the key to be set to.
	 * @since 1.0.0
	 */
	public void setByte(String key, byte value) {
		this.setString(key, "" + value);
	}
	
	/**
	 * Get the short associated with the given key.
	 * 
	 * @param key The key of the value to get.
	 * @param defaultValue The default value to use and store if no value is found.
	 * @return The gotten short.
	 * @since 1.0.0
	 */
	public short getShort(String key, short defaultValue) {
		return Short.parseShort(this.getString(key, "" + defaultValue));
	}
	
	/**
	 * Set the value of the given key.
	 * 
	 * @param key The key to be set.
	 * @param value The value for the key to be set to.
	 * @since 1.0.0
	 */
	public void setShort(String key, short value) {
		this.setString(key, "" + value);
	}
	
	/**
	 * Get the integer associated with the given key.
	 * 
	 * @param key The key of the value to get.
	 * @param defaultValue The default value to use and store if no value is found.
	 * @return The gotten integer.
	 * @since 1.0.0
	 */
	public int getInt(String key, int defaultValue) {
		return Integer.parseInt(this.getString(key, "" + defaultValue));
	}
	
	/**
	 * Set the value of the given key.
	 * 
	 * @param key The key to be set.
	 * @param value The value for the key to be set to.
	 * @since 1.0.0
	 */
	public void setInt(String key, int value) {
		this.setString(key, "" + value);
	}
	
	/**
	 * Get the float associated with the given key.
	 * 
	 * @param key The key of the value to get.
	 * @param defaultValue The default value to use and store if no value is found.
	 * @return The gotten float.
	 * @since 1.0.0
	 */
	public byte getFloat(String key, float defaultValue) {
		return Byte.parseByte(this.getString(key, "" + defaultValue));
	}
	
	/**
	 * Set the value of the given key.
	 * 
	 * @param key The key to be set.
	 * @param value The value for the key to be set to.
	 * @since 1.0.0
	 */
	public void setFloat(String key, float value) {
		this.setString(key, "" + value);
	}
	
	/**
	 * Get the long associated with the given key.
	 * 
	 * @param key The key of the value to get.
	 * @param defaultValue The default value to use and store if no value is found.
	 * @return The gotten long.
	 * @since 1.0.0
	 */
	public long getLong(String key, long defaultValue) {
		return Long.parseLong(this.getString(key, "" + defaultValue));
	}
	
	/**
	 * Set the value of the given key.
	 * 
	 * @param key The key to be set.
	 * @param value The value for the key to be set to.
	 * @since 1.0.0
	 */
	public void setLong(String key, long value) {
		this.setString(key, "" + value);
	}
	
	/**
	 * Get the double associated with the given key.
	 * 
	 * @param key The key of the value to get.
	 * @param defaultValue The default value to use and store if no value is found.
	 * @return The gotten double.
	 * @since 1.0.0
	 */
	public double getDouble(String key, double defaultValue) {
		return Double.parseDouble(this.getString(key, "" + defaultValue));
	}
	
	/**
	 * Set the value of the given key.
	 * 
	 * @param key The key to be set.
	 * @param value The value for the key to be set to.
	 * @since 1.0.0
	 */
	public void setDouble(String key, double value) {
		this.setString(key, "" + value);
	}
}
