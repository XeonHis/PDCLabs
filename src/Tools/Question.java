package Tools;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author paulalan
 * @create 2019/11/13 13:47
 */
public class Question implements Serializable
{
	private HashMap<String, Object> questionDetail = new HashMap<>();

	public void setId(int id)
	{
		questionDetail.put("id", id);
	}

	public int getId()
	{
		return (int) questionDetail.get("id");
	}

	public void setQuestion(String question)
	{
		questionDetail.put("question", question);
	}

	public String getQuestion()
	{
		return (String) questionDetail.get("question");
	}

	public void setAnswer(String answer)
	{
		questionDetail.put("answer", answer);
	}

	public String getAnswer()
	{
		return (String) questionDetail.get("answer");
	}

	public void setType(int type)
	{
		questionDetail.put("type", type);
	}

	public int getType()
	{
		return (int) questionDetail.get("type");
	}

	public void setAnswerNum(int answerNum)
	{
		questionDetail.put("answerNum", answerNum);
	}

	public int getAnswerNum()
	{
		return (int) questionDetail.get("answerNum");
	}

	public void setItems(int id, String question, String answer, int type, int answerNum)
	{
		questionDetail.put("id", id);
		questionDetail.put("question", question);
		questionDetail.put("answer",answer);
		questionDetail.put("type", type);
		questionDetail.put("answerNum", answerNum);
	}
}
