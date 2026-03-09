pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                bat 'mvn -B clean verify'
            }
        }

        stage('Compose Config') {
            steps {
                bat 'docker compose config'
            }
        }

        stage('Start DB') {
            steps {
                bat 'docker rm -f calculator-db || exit 0'
                bat 'docker volume rm inclassweek7_db_data || exit 0'
                bat 'docker compose up -d db'
            }
        }

        stage('Verify DB Schema') {
            steps {
                bat 'timeout /t 20 /nobreak'
                bat 'docker compose logs db --tail 200'
                bat 'docker exec calculator-db mariadb -uroot -pTest12 -e "USE calc_data; SHOW TABLES; DESCRIBE calc_results;"'
            }
        }
    }
}

