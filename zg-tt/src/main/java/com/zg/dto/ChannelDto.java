package com.zg.dto;

import java.util.List;

import com.zg.domain.Channel;
import com.zg.domain.ContentConfig;

/**
 * 频道传输层
 * @author yansheng
 *
 */
public class ChannelDto {
	
	private Channel channel;
	private List<ContentConfig> contentConfig;
	private ContentConfig  backupConfig;
	
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public List<ContentConfig> getContentConfig() {
		return contentConfig;
	}
	public void setContentConfig(List<ContentConfig> contentConfig) {
		this.contentConfig = contentConfig;
	}
	public ContentConfig getBackupConfig() {
		return backupConfig;
	}
	public void setBackupConfig(ContentConfig backupConfig) {
		this.backupConfig = backupConfig;
	}
	
	
	
	
}
