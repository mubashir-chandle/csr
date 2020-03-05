package com.csrapp.csr.viewstreams.jobmodel

class JobDataSource {
    companion object {
        fun getJobs(streamId: String): ArrayList<Job> {
            val jobs = ArrayList<Job>()
            when (streamId) {
                "agriculture" -> {
                    jobs.add(Job("Agriculture #1", streamId, "description"))
                    jobs.add(Job("Agriculture #2", streamId, "description"))
                    jobs.add(Job("Agriculture #3", streamId, "description"))
                    jobs.add(Job("Agriculture #4", streamId, "description"))
                    jobs.add(Job("Agriculture #5", streamId, "description"))
                    jobs.add(Job("Agriculture #6", streamId, "description"))
                    jobs.add(Job("Agriculture #7", streamId, "description"))
                    jobs.add(Job("Agriculture #8", streamId, "description"))
                    jobs.add(Job("Agriculture #9", streamId, "description"))
                    jobs.add(Job("Agriculture #10", streamId, "description"))
                    jobs.add(Job("Agriculture #11", streamId, "description"))
                    jobs.add(Job("Agriculture #12", streamId, "description"))
                }
                "arts" -> {
                    jobs.add(Job("Arts #1", streamId, "description"))
                    jobs.add(Job("Arts #2", streamId, "description"))
                    jobs.add(Job("Arts #3", streamId, "description"))
                    jobs.add(Job("Arts #4", streamId, "description"))
                    jobs.add(Job("Arts #5", streamId, "description"))
                    jobs.add(Job("Arts #6", streamId, "description"))
                    jobs.add(Job("Arts #7", streamId, "description"))
                    jobs.add(Job("Arts #8", streamId, "description"))
                    jobs.add(Job("Arts #9", streamId, "description"))
                    jobs.add(Job("Arts #10", streamId, "description"))
                    jobs.add(Job("Arts #11", streamId, "description"))
                    jobs.add(Job("Arts #12", streamId, "description"))
                }
                "commerce" -> {
                    jobs.add(Job("Commerce #1", streamId, "description"))
                    jobs.add(Job("Commerce #2", streamId, "description"))
                    jobs.add(Job("Commerce #3", streamId, "description"))
                    jobs.add(Job("Commerce #4", streamId, "description"))
                    jobs.add(Job("Commerce #5", streamId, "description"))
                    jobs.add(Job("Commerce #6", streamId, "description"))
                    jobs.add(Job("Commerce #7", streamId, "description"))
                    jobs.add(Job("Commerce #8", streamId, "description"))
                    jobs.add(Job("Commerce #9", streamId, "description"))
                    jobs.add(Job("Commerce #10", streamId, "description"))
                    jobs.add(Job("Commerce #11", streamId, "description"))
                    jobs.add(Job("Commerce #12", streamId, "description"))
                }
                "fine_arts" -> {
                    jobs.add(Job("Fine Arts #1", streamId, "description"))
                    jobs.add(Job("Fine Arts #2", streamId, "description"))
                    jobs.add(Job("Fine Arts #3", streamId, "description"))
                    jobs.add(Job("Fine Arts #4", streamId, "description"))
                    jobs.add(Job("Fine Arts #5", streamId, "description"))
                    jobs.add(Job("Fine Arts #6", streamId, "description"))
                    jobs.add(Job("Fine Arts #7", streamId, "description"))
                    jobs.add(Job("Fine Arts #8", streamId, "description"))
                    jobs.add(Job("Fine Arts #9", streamId, "description"))
                    jobs.add(Job("Fine Arts #10", streamId, "description"))
                    jobs.add(Job("Fine Arts #11", streamId, "description"))
                    jobs.add(Job("Fine Arts #12", streamId, "description"))
                }
                "health" -> {
                    jobs.add(Job("Health and Life Sciences #1", streamId, "description"))
                    jobs.add(Job("Health and Life Sciences #2", streamId, "description"))
                    jobs.add(Job("Health and Life Sciences #3", streamId, "description"))
                    jobs.add(Job("Health and Life Sciences #4", streamId, "description"))
                    jobs.add(Job("Health and Life Sciences #5", streamId, "description"))
                    jobs.add(Job("Health and Life Sciences #6", streamId, "description"))
                    jobs.add(Job("Health and Life Sciences #7", streamId, "description"))
                    jobs.add(Job("Health and Life Sciences #8", streamId, "description"))
                    jobs.add(Job("Health and Life Sciences #9", streamId, "description"))
                    jobs.add(Job("Health and Life Sciences #10", streamId, "description"))
                    jobs.add(Job("Health and Life Sciences #11", streamId, "description"))
                    jobs.add(Job("Health and Life Sciences #12", streamId, "description"))
                }
                "technical" -> {
                    jobs.add(Job("Technical #1", streamId, "description"))
                    jobs.add(Job("Technical #2", streamId, "description"))
                    jobs.add(Job("Technical #3", streamId, "description"))
                    jobs.add(Job("Technical #4", streamId, "description"))
                    jobs.add(Job("Technical #5", streamId, "description"))
                    jobs.add(Job("Technical #6", streamId, "description"))
                    jobs.add(Job("Technical #7", streamId, "description"))
                    jobs.add(Job("Technical #8", streamId, "description"))
                    jobs.add(Job("Technical #9", streamId, "description"))
                    jobs.add(Job("Technical #10", streamId, "description"))
                    jobs.add(Job("Technical #11", streamId, "description"))
                    jobs.add(Job("Technical #12", streamId, "description"))
                }
                "uniformed_services" -> {
                    jobs.add(Job("Uniformed Services #1", streamId, "description"))
                    jobs.add(Job("Uniformed Services #2", streamId, "description"))
                    jobs.add(Job("Uniformed Services #3", streamId, "description"))
                    jobs.add(Job("Uniformed Services #4", streamId, "description"))
                    jobs.add(Job("Uniformed Services #5", streamId, "description"))
                    jobs.add(Job("Uniformed Services #6", streamId, "description"))
                    jobs.add(Job("Uniformed Services #7", streamId, "description"))
                    jobs.add(Job("Uniformed Services #8", streamId, "description"))
                    jobs.add(Job("Uniformed Services #9", streamId, "description"))
                    jobs.add(Job("Uniformed Services #10", streamId, "description"))
                    jobs.add(Job("Uniformed Services #11", streamId, "description"))
                    jobs.add(Job("Uniformed Services #12", streamId, "description"))
                }
            }

            return jobs
        }
    }
}