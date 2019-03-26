# DotNet Core Docker file

```
FROM microsoft/dotnet:2.1-aspnetcore-runtime AS base
WORKDIR /app
EXPOSE 80

FROM microsoft/dotnet:2.1-sdk AS build
WORKDIR /src
COPY DotNetDockerTest/DotNetDockerTest.csproj DotNetDockerTest/
RUN dotnet restore DotNetDockerTest/DotNetDockerTest.csproj
COPY . .
WORKDIR /src/DotNetDockerTest
RUN dotnet build DotNetDockerTest.csproj -c Release -o /app

FROM build AS publish
RUN dotnet publish DotNetDockerTest.csproj -c Release -o /app

FROM base AS final
WORKDIR /app
COPY --from=publish /app .
ENTRYPOINT ["dotnet", "DotNetDockerTest.dll"]
```