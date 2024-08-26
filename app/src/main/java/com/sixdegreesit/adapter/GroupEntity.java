package com.sixdegreesit.adapter;

import java.util.ArrayList;
import java.util.List;

public class GroupEntity {
	public String questionType;
	public String question;
	public String questionId;
	public String answer;
	public String isMandatory;
	
	public List<GroupItemEntity> GroupItemCollection;

	public GroupEntity() {
		GroupItemCollection = new ArrayList<GroupItemEntity>();
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public class GroupItemEntity {
		public String question;
		public String questionId;
		public String questionType;
		public String isMandatory;
		public String isChecked;

		public String getQuestion() {
			return question;
		}

		public void setQuestion(String question) {
			this.question = question;
		}

		public String getQuestionId() {
			return questionId;
		}

		public void setQuestionId(String questionId) {
			this.questionId = questionId;
		}

		public String getQuestionType() {
			return questionType;
		}

		public void setQuestionType(String questionType) {
			this.questionType = questionType;
		}

		public String getIsMandatory() {
			return isMandatory;
		}

		public void setIsMandatory(String isMandatory) {
			this.isMandatory = isMandatory;
		}

		public String getIsChecked() {
			return isChecked;
		}

		public void setIsChecked(String isChecked) {
			this.isChecked = isChecked;
		}

	}
}
