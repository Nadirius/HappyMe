package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers;

public class AppFirebase {
    public static class Collections{
        public static final String USERS = "users";
        public static final String TEAMS = "teams";
        public static final String ALERTS = "alerts";
    }
    
    public static class SubCollections{
        public static final String USER_TEAMS = "user_teams";
        public static final String TEAM_USERS = "team_users";
        public static final String TEAM_ALERTS = "team_alerts";
        public static final String ALERT_USERS = "alert_users";
    }
    
    public static class User{
        public static final String MOBILE = "mobile";
        public static final String GENDER = "gender";
        public static final String PROFILE_COMPLETED = "profileCompleted";
        public static final String PROFILE_IMAGE = "img";
    }
    
    public static class Team{
        public static final String ID = "id";
        public static final String OWNER = "owner";
        public static final String NAME = "name";
        public static final String CITY = "city";
        public static final String DESCRIPTION = "description";
        public static final String TEAM_IMAGE = "img";
        public static final String TEAM_MEMBERS = "members";
        public static final String TEAM_IS_PUBLIC = "ispublic";
    }
}
