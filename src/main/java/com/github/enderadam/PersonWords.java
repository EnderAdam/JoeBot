package com.github.enderadam;

import java.util.List;

public class PersonWords {
    public String name;
    public List<Words> words;

    public PersonWords(String name, List<Words> words) {
        this.name = name;
        this.words = words;
    }

    public PersonWords(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonWords)) return false;

        PersonWords words = (PersonWords) o;

        return name.equals(words.name);
    }

}

class Words {
    public final String word;
    public int count;

    public Words(String word, int count) {
        this.word = word;
        this.count = count;
    }

    public Words(String word) {
        this.word = word;
    }

    public void addOne() {
        this.count++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Words)) return false;

        Words words = (Words) o;

        return word.equals(words.word);
    }
}
