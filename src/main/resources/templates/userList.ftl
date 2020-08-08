<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>用户集合</title>
</head>
<body>
<#if userList??>
    <#list userList as user>
    <p>${user.id}, ${user.userName}, ${user.birthday?string("yyyy-MM-dd")}</p>
    </#list>
</#if>
</body>
</html>