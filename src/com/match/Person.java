package com.match;

import com.match.Organ;
import java.sql.*;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

public class Person {

	static Connection con = null;
	
	private String firstName, lastName, bloodtype, birthdate;
	private int doctor;

	private String[] needs;
	private String[] donations;

	//default constructor
	public Person() {

	}

	//constructor for person if only need to display the name
	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	//constructor for person with all initial fields
	public Person(String firstName, String lastName, String birthdate, String bloodtype) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.setBirthdate(birthdate);
		this.setBloodtype(bloodtype);
	}

	//constructor for person with needs or donations
	public Person(String firstName, String lastName, String birthdate, String bloodtype, int doctor, String[] needOrDon, boolean needed) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.setBirthdate(birthdate);
		this.setBloodtype(bloodtype);
		this.setDoctor(doctor);
		if (needed) {
			this.setNeeds(needOrDon);
		} else {
			this.setDonations(needOrDon);
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public String getBloodtype() {
		return bloodtype;
	}

	public void setBloodtype(String bloodtype) {
		this.bloodtype = bloodtype;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public int getDoctor() {
		return doctor;
	}

	public void setDoctor(int doctor) {
		this.doctor = doctor;
	}

	public String[] getNeeds() {
		return needs;
	}

	public void setNeeds(String[] needs) {
		this.needs = needs;
	}

	public String[] getDonations() {
		return donations;
	}

	public void setDonations(String[] donations) {
		this.donations = donations;
	}

	/* Sample test to ensure frontend-backend connection */
	public static String sampleTest() {
			return "Success!";
	}
		
	/*
	Return an array of all the people in the person table
	Implement SQL call via JDBC to the database to get all of the people
	Webpage only displays the first and last names of all people.
	*/
    public static Person[] getPeople() {

		con = getConnection();

		if (con == null) {
			Person failed = new Person("Connection", "Failed");
			return new Person[] { failed };
		}

		Statement stmt = null;
		String query = "select first_name, last_name from person";

		//ResultSetMetaData rsmd = rs.getMetaData();
		//int columns = rsmd.getColumnCount();

		ArrayList<Person> list = new ArrayList<Person>();

		try {
		    //creating statements
		    stmt = con.createStatement();
		    ResultSet rs = stmt.executeQuery(query);

		    while (rs.next()) {
			list.add(new Person(rs.getString("first_name"), rs.getString("last_name")));
		    }
		} catch (SQLException e) {
		    System.out.println("Invalid Query or Type Casting");
		} /*finally {
		    if (stmt != null) { stmt.close(); }
		    } */
		Person[] names = list.toArray(new Person[list.size()]);

		return names;

	}

	/* 
	For every person record in the database, search each of its character fields to see if input query is a substring of any of them
	Return all relevant data that matches with every char/varchar column, such as substring of first/last name or blood type 
	This method is similar to a search engine that finds all relevant users when given a keyword  
	
	This method only returns first and last name(s).

	If no rows in the database are found with a substring match, an empty array of Person is returned.
	*/
	public static Person[] getPersonSearch(String query) {

		con = getConnection();

		if (con == null) {
			Person failed = new Person("Connection", "Failed");
			return new Person[] { failed };
		}

		//Statement stmt = null;
        String sql = "select distinct first_name, last_name, blood_type from person where first_name like ? or last_name like ? or blood_type like ?";
		PreparedStatement pstmt = null;

        ArrayList<Person> list = new ArrayList<Person>();

        try {
		    //creating statements
            //pstmt = con.createStatement();
		    pstmt = con.prepareStatement(sql);
		    pstmt.setString(1,"%" + query + "%");
		    pstmt.setString(2, "%" + query + "%");
		    pstmt.setString(3, "%" + query + "%");
            ResultSet rs = pstmt.executeQuery();

		    while (rs.next()) {
		    	list.add(new Person(rs.getString("first_name"), rs.getString("last_name")));
            }
        } catch (SQLException e) {
            System.out.println("Invalid Query or Type Casting");
        }

		Person[] names = list.toArray(new Person[list.size()]);

		return names;
	}

	/*
	This method returns a Person object with all of its fields instantiated
	for the person with the given id in the person table. 
	Since ID is a unique identifier, only one person is returned
	Return a person with the first name "No" and the last name "Matches" if
	the person with the id does not yet exist as search result.
	*/
	public static Person getPerson(String pid) {

		con = getConnection();

		if (con == null) {
			Person failed = new Person("Connection", "Failed");
			return failed;
		}

		Statement stmt = null;
                String query = "select first_name, last_name from person where id = " + pid;
		//rs.setInt(1, pid);

                ArrayList<Person> list = new ArrayList<Person>();

                try {
                    //creating statements
                    stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
		    //rs.setInt(1, pid);
                    while (rs.next()) {
                        list.add(new Person(rs.getString("first_name"), rs.getString("last_name")));
                    }
                } catch (SQLException e) {
                	System.out.println("Invalid Query or Type Casting");
                } /*finally {
                    if (stmt != null) { stmt.close(); }
                    } */
                Person[] names = list.toArray(new Person[list.size()]);

		if (list.isEmpty()) {
		    return new Person("No", "Matches");
		}
		else {
		    return names[0];
		}
	}

	/*
	This method adds a person to the database with all of the fields specified.
	If the person cannot be inserted, return -1, otherwise return the id of the person.
	*/
	public static int addPerson(String first, String last, String birthdate, String bloodtype) {

		con = getConnection();

		if (con == null) {
			
			return -1;
		}

		String sql = "insert into person"
		    + "(first_name, last_name, birthdate, blood_type, doctor_id) values"
		    + "(?,?,?,?, null)";
		String idsql = "select * from person";
		PreparedStatement pstmt = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date bdate = null;
		try {
		    bdate = formatter.parse(birthdate);
		} catch (ParseException e) {e.printStackTrace(); }
		PreparedStatement idstmt = null;

		try {
		    //pstmt = con.createStatement();
		    java.sql.Date sqlDate = new java.sql.Date(bdate.getTime());
		    pstmt = con.prepareStatement(sql);
		    pstmt.setString(1, first);
		    pstmt.setString(2, last);
		    pstmt.setDate(3, sqlDate);
		    pstmt.setString(4, bloodtype);
		    pstmt.executeUpdate();
		    System.out.println("1");
		    System.out.println("1");

		    idstmt = con.prepareStatement(idsql);
		    ResultSet rs = idstmt.executeQuery();
		    System.out.println("1");
		    int count = 0;
		    while (rs.next()) {
			count++;
		    }
		    return count;

		} catch (SQLException e) {
		    System.out.println("Invalid Query or Type Casting");
		}

		return -1;
	}

	/*
	This method returns a list of all organ donors with organ matches in the database for the person with the given id based on the
	blood type compatibility and type of organ needed. 
	If the person needs more than one organ, return all matches for all organs needed.
	The person must be excluded because he cannot match with him/herself.
	If the person with id does not exist, return a Person array with one person with the first name "No" and last name "Matches" to be displayed
	If the person has no matches, return an empty Person array.
	If the person doesn't need an organ, return an empty Person array.
	*/
	public static Person[] getOrganMatches(String id) {

		con = getConnection();
		// If that fails, send dummy entries
		if (con == null) {
			System.out.println("Connection failed");
			Person failed = new Person("Connection", "Failed");
			return new Person[] { failed };
		}

		PreparedStatement pstmt = null;
                String query = "with orgs(firstname, lastname, org, btype) as (select distinct first_name, last_name, organ, blood_type from person, needs where id = ?),"
		    + "allorgs(fname, lname, orga, bltype) as (select distinct first_name, last_name, organ, blood_type from available inner join person on person.organ = vailable.organ)"
		    + "select distinct fname, lname from orgs, allorgs where org = orga and btype = bltype and fname != firstname and lname != lastname";
		ArrayList<Person> organList = new ArrayList<Person>();
		try {
		    pstmt = con.prepareStatement(query);
		    pstmt.setInt(1, Integer.parseInt(id));
		    ResultSet rs = pstmt.executeQuery();
		    while (rs.next()) {
			organList.add(new Person(rs.getString("fname"),rs.getString("lname")));
                    }
		    if (organList.isEmpty()) {
			Person[] name = new Person[1];
			name[0] = new Person("No", "Match");
			return name;
		}

		} catch (SQLException e) {
			System.out.println("Invalid Query or Type Casting");
        }
        Person[] names = organList.toArray(new Person[organList.size()]);

		return names;
	}

    /*
	This method adds a needed organ 'o' into the database. The person who needs
	the organ is given by his/her id. The person cannot require the same organ more than once.
	Return 1 on success or -1 on failure.
	*/
	public static int addNeededOrgan(Organ o, int pid, String byDate) {

		con = getConnection();

		if (con == null) {
			System.out.println("Connection Failed");
			return -1;
		}

		String sql = "if not exists(select 1 from needed where organ = ?)" +
		    "insert into needs(id, organ, by) " +
		    "values(?, ?, ?)";

		PreparedStatement pstmt = null;
		int i = 0;

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                Date bdate = null;
		try {
		    bdate = formatter.parse(byDate);
		} catch (ParseException e) {e.printStackTrace(); }

		try {
			@SuppressWarnings("deprecation")
			java.sql.Date sqlDate = new java.sql.Date(bdate.getDate());
		    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, o.toString());
		    pstmt.setInt(2, pid);
		    pstmt.setString(3, o.toString());
		    pstmt.setDate(4, sqlDate);
		    i = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Invalid Query or Type Casting");
        }

		if (i == 0 || i == -1) {
		    return -1;
		}
		else {
		    return 1;
		}
	}
	
	/*
	This method adds an available organ o into the database. The organ donor who can give
	the organ is given by his/her id. A person must not be able to offer more than one of each organ.
	Return 1 on success or -1 on failure.
	*/
	public static int addAvailableOrgan(Organ o, int pid) {

		con = getConnection();

		if (con == null) {
			System.out.println("Connection Failed");
			return -1;
		}

		String sql = "if not exists(select 1 from available where organ = ?) then"
                    + "insert into available(id, organ) " + "values(?, ?);"
                    + "end if;";

                PreparedStatement pstmt = null;
                int i = 0;

                try {
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, o.toString());
                    pstmt.setInt(2, pid);
                    pstmt.setString(3, o.toString());
                    i = pstmt.executeUpdate();
                } catch (SQLException e) {
                	System.out.println("Invalid Query or Type Casting");
                }

                if (i == 0 || i == -1) {
                    return -1;
                }
                else {
                    return 1;
                }
	}

	/*
	This method is to check if person donorID can satisfy a person's needID's needs for
	an organ, such that the organ must match and person's blood type must be
	compatible with donor's.
	*/
	public static boolean computeOrganMatch (int needID, int donorID){

		con = getConnection();

		if (con == null) {
			System.out.println("Connection Failed");
			return false;
		}

		boolean organMatch = false;

		String sql = "select norgan.bloodtype as nbloodtype, dorgan.bloodtype as dbloodtype from ("
		    + "(select organ from needs where id = ?) as norgan"
		    + "inner join (select organ, bloodtype from available where id = ?) as dorgan)"
		    +"on norgan.organ = dorgan.organ);";

                PreparedStatement pstmt = null;

                try {
                    pstmt = con.prepareStatement(sql);
                    pstmt.setInt(1, needID);
                    pstmt.setInt(2, donorID);
        		    //System.out.println("test2");
        		    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                    	if (checkBloodType(rs.getString("nbloodtype"), rs.getString("dbloodtype"))) {
                    		organMatch = true;
                    	}
                    }
                } catch (SQLException e) {
                	System.out.println("Invalid Query or Type Casting");
                }

        return organMatch;
	}

	/* 
	This method automatically assigns a doctor to a person when they sign up for the first time.
	A doctor is assigned to a person by updating the person's doctor_id field with
	the doctor with the fewest patients. This will ensure that patients are distributed equally.
	The number of patients is defined as the number of people assigned to that doctor through their doctor_id field. 
	If multiple doctors have the same number of fewest patients, choose the doctor with the smallest id.
	*/
	public static int getDoctorFirst (int pid) {

	    con = getConnection();

	    if (con == null) {
	    	System.out.println("Connection Failed");
	        return -1;
	    }
	    String query = "select doctor_id as did"
		+ "from (select doctor_id, count(*) as counted from doctors"
		+ "group by doctor_id order by counted desc, id asc) limit 1";
	    String sql = "update person set doctor_id = ? where id = ?";
	    PreparedStatement pstmt = null;
	    PreparedStatement dstmt = null;
	    int doc_id = 0, i = 0;
	    try {
	    	pstmt = con.prepareStatement(query);
	    	ResultSet rs = pstmt.executeQuery();
	    	while (rs.next()) {
		    doc_id = rs.getInt("doctor_id");
	    	}
	    	dstmt = con.prepareStatement(sql);
	    	dstmt.setInt(1, doc_id);
	    	dstmt.setInt(2, pid);
	    	i = dstmt.executeUpdate();
	    } catch (SQLException e) {
	    	System.out.println("Invalid Query or Type Casting");
	    }

	    if (i == 0 || i == -1) {
	    	return -1;
	    } 
	    else {
	    	return 1;
	    }
	}

	/*
	This is method to assign a doctor to the given person following an organ
	request or offer. This method is invoked each time a person requests or offers an organ.
	To ensure the patients are evenly distributed, find the doctor with the
	fewest patients, who also has the same specialty as their requested/offered organ.
	Most experienced doctor should be the first option (defined by experience attribute). 
	If multiple doctors are still tied, a doctor is chosen with the first name in alphabetical order. 
	If they are still tied choose the one with the smaller id.
	*/
	public static int getDoctorUpdate (Organ o, int pid) {
		con = getConnection();

	    if (con == null) {
	    	System.out.println("Connection Failed");
	        return -1;
	    }

	    int did = 0;

		String sql = "select id, count(*) as pCounted from doctors"
		    + "where specialty = ? and id not in (select doctor_id from person where id = ?)"
		    + "group by id"
		    + "order by experience desc, name asc, id asc, pCounted asc limit 1;";

		String updateSQL = "update person set doctor_id = ? where id = ?";
                PreparedStatement pstmt = null;
                PreparedStatement dstmt = null;

                try {
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, o.toString());
                    pstmt.setInt(2, pid);
        		    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                    	did = rs.getInt("id");
                    	break;
                    }
                    dstmt = con.prepareStatement(updateSQL);
                    dstmt.setInt(1, did);
                    dstmt.setInt(2, pid);
                    dstmt.executeUpdate();
                } catch (SQLException e) {
                	System.out.println("Invalid Query or Type Casting");
                }

		return did;
	}

	/*
	Get the name of a patient's doctor, given the patient's id.
	*/
	public static String getDoctorName(int pid){
		con = getConnection();

		if (con == null) {
			System.out.println("Connection Failed");
			return "No connection";
		}

		String doctorName = "";

		String sql = "select doctors.name from ("
		    + "(select * from person where id = ?)"
		    + "inner join (select * from doctors)"
		    +"on person.doctor_id = doctors.id);";

                PreparedStatement pstmt = null;

                try {
                    pstmt = con.prepareStatement(sql);
                    pstmt.setInt(1, pid);
        		    //System.out.println("test2");
        		    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                    	doctorName = rs.getString("doctors.name");
                    	break;
                    }
                } catch (SQLException e) {
                	System.out.println("Invalid Query or Type Casting");
                }

        return doctorName;
	}

	private static Connection getConnection() {
		// Return existing connection after first call
		if (con != null) {
			return con;
		}
		// Set up postgres connection
		con = getConnection();
		// If that fails, attempt to connect to a local postgres server
		if (con == null) {
			con = getLocalConnection();
		}
		// If that fails, give up
		if (con == null) {
			return null;
		}
		// Attempt to initialize the database on first connection
		//initDatabase();
		return con;
	}

	/* 
	Connect to the local database for development purposes
	*/
	private static Connection getLocalConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection(
			"jdbc:postgresql://localhost/donormatchapp", //connURL
			"matchmaker", //username
			"kingofkings"); //password
			return con;
		}
		catch (ClassNotFoundException e) { 
			System.out.println("Class Not Found!");
		}
		catch (SQLException e) { 
			System.out.println("SQL Setup Failed");
		}
		return null;
	}

	/*
		This given function returns true if donType can be given to recType successfully
		and false otherwise.
	 */
	public static boolean checkBloodType(String recType, String donType) {

		//start with two catch-alls
		if (donType.equals("O-") || recType.equals("AB+")) {
			return true;
		}

		//all types can donate to themselves
		if (donType.equals(recType)) {
			return true;
		}

		//A-, O+, AB+, O-, B- are satisfied by catch-alls

		//check A+
		if (recType.equals("A+")) {
			if (donType.equals("A-") || donType.equals("O+")) {
				return true;
			} else {
				return false;
			}
		}


		//check B+
		if (recType.equals("B+")) {
			if (donType.equals("B-") || donType.equals("O+")) {
				return true;
			} else {
				return false;
			}
		}

		//check AB-
		if (recType.equals("AB-")) {
			if (donType.equals("A-") || donType.equals("B-")) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

        public static void main(String [] args) {
	    /*
	    Person[] persons1 = Person.getPeople();
	    for (Person p : persons1) {
		System.out.println(p.getFirstName() + " " + p.getLastName());
	    }
	    System.out.println();

	    Person p2 = Person.getPerson("1234");
	    System.out.println(p2.getFirstName() + " " + p2.getLastName());

	    Person p3 = Person.getPerson("4");
	    System.out.println(p3.getFirstName() + " " + p3.getLastName());
	    System.out.println();

	    Person[] p4 = Person.getPersonSearch("ex");
	    for (Person x : p4) {
		System.out.println(x.getFirstName() + " " + x.getLastName());
	    }
	    System.out.println();
	    */
	    /*
	    int p5 = Person.addPerson("Hong", "Wu", "12/15/1992", "A-");
	    System.out.println(p5);
	    int p6 = Person.addPerson("Spider", "Man", "08/13/1987", "B+");
	    System.out.println(p6);

	    Person[] persons2 = Person.getPeople();
	    for (Person p : persons2) {
		System.out.println(p.getFirstName() + " " + p.getLastName());
	    }
	    */
	    /*
	    Person[] persons3 = Person.getOrganMatches("1234");
	    for (Person p: persons3) {
		System.out.println(p.getFirstName() + " " + p.getLastName());
	    }
	    */
	    //Organ org = Organ.Liver;
	    //int i = Person.addNeededOrgan(org, 11, "02/18/2020");
	    //System.out.println(i);

//	    Organ org = Organ.Liver;
//            int i = Person.addAvailableOrgan(org, 14);
//            System.out.println(i);

	}


}
