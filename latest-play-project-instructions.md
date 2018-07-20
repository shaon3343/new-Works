#creating play project with sbt
1. Install sbt and add path to environment.
2. Traverse to the directory where you want to create play project.
3. With this command you can initiate creating a play project: sbt new playframework/play-java-seed.g8
4. provide necessary names for projects and versions.
5. CD into that created project and run "sbt compile"
5. sbt run will run this project in default 9000 port and it will add some default filters with it like URL filter.
6. You can disable all these filtering in conf file. by this line: play.filters.enabled=[]
