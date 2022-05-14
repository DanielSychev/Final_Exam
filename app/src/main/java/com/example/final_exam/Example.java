package com.example.final_exam;

public class Example {
    String text="";
    String answer="";

    public Example(String text, String answer) {
        this.text = text;
        this.answer = answer;
    }
    public Example(){
    }

    public String getText() {
        return text;
    }

    public String getAnswer() {
        return answer;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
