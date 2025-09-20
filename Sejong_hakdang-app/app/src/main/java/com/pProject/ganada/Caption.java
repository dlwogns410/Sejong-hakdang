package com.pProject.ganada;

import java.util.HashMap;
import java.util.Map;

public class Caption {
    private String type;  // "text" 또는 "object"
    private String kind;  // 사물 인식에서의 종류 (object recognition 결과)
    private String message;  // 캡션 내용 (사물 또는 이미지 캡션)
    private Map<String, String> word_example;  // 텍스트 인식에서 단어와 예문

    // Constructor
    public Caption() {
        word_example = new HashMap<>();
    }

    // Getter 및 Setter
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getWordExample() {
        return word_example;
    }

    public void setWordExample(Map<String, String> word_example) {
        this.word_example = word_example;
    }

    // 'word'의 Getter와 Setter
    public String getWord() {
        return word_example != null && word_example.containsKey("word") ? word_example.get("word") : "알 수 없음";
    }

    public void setWord(String word) {
        if (this.word_example != null) {
            this.word_example.put("word", word != null ? word : "알 수 없음");
        }
    }

    // 'example'의 Getter와 Setter
    public String getExample() {
        return word_example != null && word_example.containsKey("example") ? word_example.get("example") : "예문 없음";
    }

    public void setExample(String example) {
        if (this.word_example != null) {
            this.word_example.put("example", example != null ? example : "예문 없음");
        }
    }

    @Override
    public String toString() {
        return "Caption{" +
                "type='" + type + '\'' +
                ", kind='" + kind + '\'' +
                ", message='" + message + '\'' +
                ", word_example=" + word_example +
                '}';
    }
}
