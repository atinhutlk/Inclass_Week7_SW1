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
                bat 'docker compose down -v'
                bat 'docker compose up -d db'
            }
        }

        stage('Verify DB Schema') {
            steps {
                bat 'docker compose logs db --tail 200'
                bat 'docker exec calculator-db mariadb -uroot -pTest12 -e "USE calc_data; SHOW TABLES; DESCRIBE calc_results;"'
            }
        }
    }
}

