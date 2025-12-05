package com.springsecurity.springsecurity.entity.student;

import com.springsecurity.springsecurity.entity.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 100)
    private String fullName;

    // 🔹 Basic Details
    @Column(length = 100)
    private String fatherName;

    @Column(length = 100)
    private String motherName;

    @Column(length = 15)
    private String contact;

    @Column(length = 10)
    private String gender;

    @Column(length = 20)
    private String dob;

    // 🔹 Academic Details
    @Column(length = 20)
    private String course;

    @Column(length = 10)
    private String year;

    @Column(length = 20)
    private String branch;

    @Column(length = 20)
    private String rollNumber;

    @Column(length = 20)
    private String scholarType;

    // 🔹 Hostel & Address Info
    @Column(length = 10)
    private String roomNumber;

    @Column(length = 255)
    private String address;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String state;

    // 🔹 S3 Profile Picture URL
    @Column(length = 512)
    private String profilePicUrl; // ✅ renamed field

    @Column(nullable = false)
    private boolean profileComplete = false;

    // 🏗 Constructors
    public Student() {}

    public Student(User user, String fullName) {
        this.user = user;
        this.fullName = fullName;
    }

    // ⚙️ Getters & Setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }

    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getScholarType() { return scholarType; }
    public void setScholarType(String scholarType) { this.scholarType = scholarType; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }

    public boolean isProfileComplete() { return profileComplete; }
    public void setProfileComplete(boolean profileComplete) { this.profileComplete = profileComplete; }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", fullName='" + fullName + '\'' +
                ", email=" + (user != null ? user.getEmail() : "null") +
                ", profilePicUrl='" + profilePicUrl + '\'' +
                ", profileComplete=" + profileComplete +
                '}';
    }
}
