package org.boreas320.demo.api.controller;

import org.boreas320.demo.api.model.GetNoteInput;
import org.boreas320.demo.api.model.GetNotesInput;
import org.boreas320.demo.api.model.Note;
import org.boreas320.demo.api.model.Person;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by xiangshuai on 2017/12/29.
 */
@RestController
@RequestMapping("/")
public class IndexController {
    @RequestMapping
    public Map<String, Object> hello() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("hello", "world");
        return map;
    }

    @RequestMapping("/getNotes")
    public List<Note> getNotes(@RequestBody GetNotesInput getNotesInput) {
        ArrayList<Note> notes = new ArrayList<>();
        Person 相帅 = new Person("相帅");
        notes.add(new Note(new Date(), "a", "日报", 相帅));
        notes.add(new Note(new Date(), "b", "周报", 相帅));
        notes.add(new Note(new Date(), "c", "月报", 相帅));
        return notes;

    }

    @RequestMapping("/getNote")
    public Note getNote(@RequestBody GetNoteInput getNoteInput) {
        return new Note(new Date(), "a", "年报", new Person("相帅"));
    }

}

