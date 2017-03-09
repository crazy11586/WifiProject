package com.jsu.po;

public class TzData {

		private String SourceMac;
		private String targetMac;
		private String frameBigClass;
		private String frameSmallClass;
		private String channel;
		private String rSSI;
		private double distance;
		
	
		
		public double getDistance() {
			return distance;
		}
		public void setDistance(double distance) {
			this.distance = distance;
		}
		public String getSourceMac() {
			return SourceMac;
		}
		public void setSourceMac(String sourceMac) {
			SourceMac = sourceMac;
		}
		public String getTargetMac() {
			return targetMac;
		}
		public void setTargetMac(String targetMac) {
			this.targetMac = targetMac;
		}
		public String getFrameBigClass() {
			return frameBigClass;
		}
		public void setFrameBigClass(String frameBigClass) {
			this.frameBigClass = frameBigClass;
		}
		public String getFrameSmallClass() {
			return frameSmallClass;
		}
		public void setFrameSmallClass(String frameSmallClass) {
			this.frameSmallClass = frameSmallClass;
		}
		public String getChannel() {
			return channel;
		}
		public void setChannel(String channel) {
			this.channel = channel;
		}
		public String getrSSI() {
			return rSSI;
		}
		public void setrSSI(String rSSI) {
			this.rSSI = rSSI;
		}
		@Override
		public String toString() {
			
			return "TzData [SourceMac=" + SourceMac +", rSSI=" + rSSI + ", distance="+ distance + "]";
		}
		
		
	
		
		
}
