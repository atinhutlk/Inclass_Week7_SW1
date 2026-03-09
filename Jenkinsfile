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
                script {
                    bat 'ping 127.0.0.1 -n 31 > nul'
                    retry(3) {
                        bat 'docker exec calculator-db mariadb -uroot -pTest12 -e "SELECT 1" || (ping 127.0.0.1 -n 6 > nul && exit 1)'
                    }
                }
                bat 'docker compose logs db --tail 200'
                bat 'docker exec calculator-db mariadb -uroot -pTest12 -e "USE calc_data; SHOW TABLES; DESCRIBE calc_results;"'
            }
        }
    }
}

