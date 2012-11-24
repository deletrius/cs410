package loclock.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

	
	//@PersistenceCapable
	@PersistenceCapable(identityType = IdentityType.APPLICATION)
	public class Calendar {
		
		@PrimaryKey
		@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
		private Long eventId;
		@Persistent
		private String userName;
		@Persistent
		private String eventName;
		@Persistent
		private String description;
		@Persistent
		private Date startDate;
		@Persistent
		private Date endDate;
		@Persistent
		private boolean canEdit;
		
		/**
		 * Basic class constructor of Calendar.
		 */
		public Calendar(){
			
			}
		
		/**
		 * Class constructor of Calendar.
		 * 
		 * @param userName the username of the account using the calendar
		 * @param eventName the name of the event
		 * @param description the description of the event
		 * @param startDate the start time of the event
		 * @param endDate the end time of the event
		 */
		public Calendar(String userName, String eventName, String description,
					Date startDate, Date endDate) {
				super();
				this.userName = userName;
				this.eventName = eventName;
				this.description = description;
				this.startDate = startDate;
				this.endDate = endDate;
			}
		
		
		/**
		 * Class constructor of Calendar
		 * 
		 * @param userName the name of the calendar user
		 * @param eventName the name of the event
		 * @param description the description of the event
		 * @param startDate the start time of the event
		 * @param endDate the end time of the event
		 * @param canEdit the authority of editing the event
		 */
		public Calendar(String userName, String eventName, String description,
				Date startDate, Date endDate, boolean canEdit) {
				super();
				this.userName = userName;
				this.eventName = eventName;
				this.description = description;
				this.startDate = startDate;
				this.endDate = endDate;
				this.canEdit = canEdit;
			}
		
		/**
		 * Get the name of the calendar user.
		 * 
		 * @return userName the username of the calendar user
		 */
		public String getUserName() {
				return userName;
			}
		
		/**
		 * Set the user of the calendar
		 * 
		 * @param userName the username of the calendar user
		 */
		public void setUserName(String userName) {
				this.userName = userName;
			}
		
		/**
		 * Get the name of the event.
		 * 
		 * @return eventName the name of the event
		 */
		public String getEventName() {
				return eventName;
			}
		
		/**
		 * Set the name of the event.
		 * 
		 * @param eventName the name of the event
		 */
		public void setEventName(String eventName) {
				this.eventName = eventName;
			}
		
		/**
		 * Get the description of the event.
		 * 
		 * @return description the description of the event
		 */
		public String getDescription() {
				return description;
			}
		
		/**
		 * Set the description for the event.
		 * 
		 * @param description the description of the event
		 */
		public void setDescription(String description) {
				this.description = description;
			}
		
		/**
		 * Get the start time of the event.
		 * 
		 * @return the start time of the event
		 */
		public Date getStartDate() {
				return startDate;
			}
		
		/**
		 * Set the start time of the event.
		 * 
		 * @param startDate the start time of the event
		 */
		public void setStartDate(Date startDate) {
				this.startDate = startDate;
			}
		
		/**
		 * Get the end time of the event.
		 * 
		 * @return endDate the end time of the event
		 */
		public Date getEndDate() {
				return endDate;
			}
		
		/**
		 * Set the end time for the event.
		 * 
		 * @param endDate the end of the event
		 */
		public void setEndDate(Date endDate) {
				this.endDate = endDate;
			}
			
		/**
		 * Get the information about whether one can edit the calendar.
		 * 
		 * @return canEdit the boolean value of whether one can edit
		 */
		public boolean isCanEdit() {
				return canEdit;
			}
		
		/**
		 * Set the authority of editing the calendar.
		 * 
		 * @param canEdit boolean value of whether one can edit calendar
		 */
		public void setCanEdit(boolean canEdit) {
				this.canEdit = canEdit;
			}

}
