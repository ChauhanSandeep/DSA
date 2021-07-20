package Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class MeetingSchedule {

    public static void main(String[] args) {
        String[][] scheduleStr = {
                {"10:00","11:00"},
                {"14:00","16:00"},
                {"23:00","23:30"}};
        List<Schedule> schedules = new ArrayList<>();
        for(int i=0; i<scheduleStr.length; i++) {
            schedules.add(new Schedule(scheduleStr[i][0], scheduleStr[i][1]));
        }

        Schedule incomingSchedule = new Schedule("11:00", "14:00");
        boolean canAccomodate = accomodata(schedules, incomingSchedule);
        System.out.println(canAccomodate);
        System.out.println(Arrays.asList(schedules));
    }

    /**
     * Add a incoming schedule  to the list of schedules.
     * @param schedules
     * @param incomingSchedule
     * @return
     */
    public static boolean accomodata(List<Schedule> schedules, Schedule incomingSchedule) {
        Collections.sort(schedules);
        if(incomingSchedule.endTime <= schedules.get(0).startTime) {
            schedules.add(0, incomingSchedule);
            return true;
        }
        for(int i=0; i<schedules.size(); i++) {
            if(schedules.get(i).endTime <= incomingSchedule.startTime && (i==schedules.size()-1 || incomingSchedule.endTime <= schedules.get(i+1).startTime)) {
                schedules.add(i+1, incomingSchedule);
                return true;
            }
        }
        return false;
    }
}

class Schedule implements Comparable<Schedule>{
    float startTime;
    float endTime;

    public Schedule(String startTime, String endTime) {
        this.startTime = getTime(startTime);
        this.endTime = getTime(endTime);
    }

    private float getTime(String time) {
        String[] timeArr = time.split(":");
        return Float.sum(Float.parseFloat(timeArr[0]), Float.parseFloat(timeArr[1])/60f);
    }

    public int compareTo(Schedule other) {
        return Float.compare(this.endTime, other.endTime);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "{", "}")
                .add("startTime=" + startTime)
                .add("endTime=" + endTime)
                .toString();
    }
}
