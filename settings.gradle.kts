rootProject.name = "qlib"
include("src:main:test")
findProject(":src:main:test")?.name = "test"
