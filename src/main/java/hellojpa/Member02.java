package hellojpa;

import javax.persistence.*;

//@Entity //jpa가 관리한다
//@Table(name = "USER") //DB table 명은 class 명을 따르지만 변경하고 싶은때
//@TableGenerator(
//        name = "MEMBER_SEQ_GENERATOR",
//        table = "MY_SEQUENCES",
//        pkColumnValue = "MEMBER_SEQ", allocationSize = 1)
public class Member02 {


    //@GeneratedValue(strategy = GenerationType.TABLE, generator = "MEMBER_SEQ_GENERATOR")
   /* @Id //pk 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable == not null
    @Column(name = "name", nullable = false) //DB column 명은 변수명을 따르지만(default), 변경하고 싶은때 (@Coulm 의 다른 속성들 unique = true, length = 10)
    private String username;

    public Member02(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }*/

    /*
    private Integer age;

    @Enumerated(EnumType.STRING) // enum type을 쓰고 싶을때 (EnumType.ORDINAL 을 쓰면 안된다(확장성에 문제가 있다))
    private RoleType roleType;

    // 자주 안씀, java8로 넘어오면서 testLocalDate, testLocalDateTime 을 쓴다
    @Temporal(TemporalType.TIMESTAMP) // TIMESTAMP ctrl click ( DATE(날짜),TIME(시간),TIMESTAMP(날짜시간))
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    private LocalDate testLocalDate;
    private LocalDateTime testLocalDateTime;

    @Lob // lob type varchar 를 넘어서는...
    private String description;

    @Transient // db랑 관계 없이 메모리상에서 쓰겠다
    private int temp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
    */
}
