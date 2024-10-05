package com.compass.challenge.domain;

/**
 * This class is a custom error response 
 */
public final class ErrorDetails {
	
	private final Long status;
	
	private final Long timestamp;
	
	private final String message;
	
	public ErrorDetails(ErrorDetailsBuilder builder) {
		this.status = builder.status;
		this.timestamp = builder.timestamp;
		this.message = builder.message;
	}
	
	public static class ErrorDetailsBuilder {
		private Long status;
		private Long timestamp;
		private String message;

		public ErrorDetailsBuilder() {
			// Empty
		}

		public ErrorDetailsBuilder(ErrorDetails errorDetails) {
			this.status = errorDetails.status;
			this.timestamp = errorDetails.timestamp;
			this.message = errorDetails.message;
		}
		
		public ErrorDetailsBuilder status(Long status) {
			this.status = status;
			return this;
		}
		
		public ErrorDetailsBuilder timestamp(Long timestamp) {
			this.timestamp = timestamp;
			return this;
		}
		
		public ErrorDetailsBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		public ErrorDetails build() {
			return new ErrorDetails(this);
		}
	}

	public Long getStatus() {
		return status;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

}
