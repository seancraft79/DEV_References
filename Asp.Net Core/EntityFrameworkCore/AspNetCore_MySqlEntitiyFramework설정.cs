

AspNetCore_MySqlEntitiyFramework설정




-- MySql DAL 에 Nuget Packge 설치
Microsoft.AspNetCore.Identity (?)
Microsoft.AspNetCore.Identity.EntityFrameworkCore
Microsoft.EntityFrameworkCore
Pomelo.EntityFrameworkCore.Mysql


-- CML에 ApplicationUser 만들기
Microsoft.AspNetCore.Identity 를 상속
public class ApplicationUser : IdentityUser
{

}



-- MysqlContext 만들기
public class MySqlContext : IdentityDbContext<ApplicationUser>
{
    public MySqlContext(DbContextOptions<MySqlContext> options) : base(options)
    {
        this.Database.EnsureCreated();
    }

    protected override void OnModelCreating(ModelBuilder builder)
    {
        base.OnModelCreating(builder);
    }

    // 해당 모델에 따라 DB Table 만들어짐
    public virtual DbSet<UserModel> KnumUser { get; set; }
}



-- appsettings.json
ConnectionStrings 설정해주고


-- startup.cs ConfigureServices에 아래 추가
services.AddDbContext<MySqlContext>(options => options.UseMySql(Configuration.GetConnectionString("ConnectionString_EF")));
services.AddIdentity<ApplicationUser, IdentityRole>().AddEntityFrameworkStores<MySqlContext>().AddDefaultTokenProviders();



-- 앱 실행해보면 바로 TABLE  만들어진 걸 확인할 수 있음





Install-Package Microsoft.EntityFrameworkCore.Tools
Install-package Pomelo.EntityFrameworkCore.Mysql.Design