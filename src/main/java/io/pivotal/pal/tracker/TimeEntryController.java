package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    TimeEntryRepository timeEntryRepository;

//
//    ResponseEntity<List<TimeEntry>> list = new ResponseEntity<List<TimeEntry>>();
//    List<TimeEntry>

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }


    @PostMapping ("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = this.timeEntryRepository.create(timeEntryToCreate);
        return new ResponseEntity(timeEntry, HttpStatus.CREATED);
    }

    @GetMapping ("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);

        if (timeEntry == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity(timeEntry, HttpStatus.OK);
        }



    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {

        List<TimeEntry> list= timeEntryRepository.list();
        return new ResponseEntity(list, HttpStatus.OK);

    }

    @PutMapping ("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry timeEntryItem) {

        TimeEntry timeEntry = this.timeEntryRepository.update(timeEntryId, timeEntryItem);

        if (timeEntry == null) {

            return new ResponseEntity(timeEntry, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity(timeEntry, HttpStatus.OK);
        }

    }

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long timeEntryId) {

//        try {
            this.timeEntryRepository.delete(timeEntryId);
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
//        }catch (NullPointerException e){
//            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
//        }

    }
}
