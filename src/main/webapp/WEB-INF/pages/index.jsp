<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Hello, World!</title>
    </head>
    <body>
        <h1>${message}</h1>
        <table>
            <tr>
                <th>ID</th>
                <th>Code</th>
                <th>Title</th>
            </tr>
            <tr>
                <th>${id}</th>
                <th>${code}</th>
                <th>${title}</th>
            </tr>
        </table>
    </body>
</html>
