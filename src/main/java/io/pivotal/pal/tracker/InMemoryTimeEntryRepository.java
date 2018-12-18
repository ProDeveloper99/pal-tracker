package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    Map<Long,TimeEntry> list = new HashMap<Long,TimeEntry>();
    //List<TimeEntry> list = new ArrayList<TimeEntry>();

    private Long count = 0L;

    public TimeEntry create(TimeEntry timeEntry) {
        list.put(++count,timeEntry);
        timeEntry.setId(count);
//        list.`add(timeEntry);
        return timeEntry;
    }

    public TimeEntry find(long timeEntryId) {
        return list.get(timeEntryId);

//        for(int i=0; i < this.list.size();i++){
//            if (timeEntryId == list.get(i).getId()){
//                return list.get(i);
//            }
//        }

//        return null;
    }

    public List<TimeEntry> list() {

//        return list;
        return new ArrayList<>(list.values());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        timeEntry.setId(id);
//        list.set((int)id-1,timeEntry);
        list.put(id, timeEntry);
        return timeEntry;
    }

    public void delete(long id) {
//        list.remove((int)id-1);
        list.remove(id);
    }
}
