package myapp

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional

@ReadOnly
class VehicleController {

    VehicleService vehicleService

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond vehicleService.list(params), model:[vehicleCount: vehicleService.count()]
    }

    def show(Long id) {
        respond vehicleService.get(id)
    }

    @Transactional
    def save(Vehicle vehicle) {
        if (vehicle == null) {
            render status: NOT_FOUND
            return
        }
        if (vehicle.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond vehicle.errors
            return
        }

        try {
            vehicleService.save(vehicle)
        } catch (ValidationException e) {
            respond vehicle.errors
            return
        }

        respond vehicle, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(Vehicle vehicle) {
        if (vehicle == null) {
            render status: NOT_FOUND
            return
        }
        if (vehicle.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond vehicle.errors
            return
        }

        try {
            vehicleService.save(vehicle)
        } catch (ValidationException e) {
            respond vehicle.errors
            return
        }

        respond vehicle, [status: OK, view:"show"]
    }

    @Transactional
    def delete(Long id) {
        if (id == null || vehicleService.delete(id) == null) {
            render status: NOT_FOUND
            return
        }

        render status: NO_CONTENT
    }
}